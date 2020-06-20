import java.io.*;
import java.util.*;
import java.nio.file.*;
import static java.nio.charset.Charset.forName;

public class Subway{
    private static HashMap<String, ArrayList<Station>> db;
    private static HashMap<String, Station> id_db;

    public static void main(String args[]) {
        if(args.length != 1) {
            System.err.println("지하철 데이터 파일을 command line에 입력해주세요.");
            System.exit(1);
        }

        Path path = Paths.get(args[0]);
        HashMap<String, ArrayList<Station>> db;

        try {
            db = build_map(path);

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String[] input = br.readLine().split(" ");
            navigate(input[0], input[1]);


        } catch (IOException e) {
            System.err.println("올바르지 않은 입력입니다");
            System.exit(1);
        }
    }

    public static HashMap<String, ArrayList<Station>> build_map(Path path) throws IOException {
        db = new HashMap<String, ArrayList<Station>>();
        id_db = new HashMap<String, Station>();

        List<String> lines = Files.readAllLines(path, forName("UTF-8"));
        Iterator<String> itr= lines.iterator();

        while(itr.hasNext()) {
            String station_info = itr.next();
            if("".equals(station_info))
                break;

            String[] terms = station_info.split(" ");
            String id = terms[0], name = terms[1];

            Station station = new Station(name);

            ArrayList<Station> near = db.get(name);
            if(near == null) {
                System.out.println(name + " 's near is empty");
                near = new ArrayList<Station>();
                db.put(name, near);
            } else {
                System.out.println(name + " 's near is NOT empty");
            }

            near.add(station);
            //현재 db 내 모든 역의 arraylist가 1개 이상 element 보유
            id_db.put(terms[0], station);
        }

        while(itr.hasNext()) {
            String time_info = itr.next();
            String[] terms = time_info.split(" ");
            String from_id = terms[0], to_id = terms[1];
            long time = Long.parseLong(terms[2]);

            Station from = id_db.get(from_id);
            Station to = id_db.get(to_id);
            //각 역의 호선별로 reachable한 정보 저장
            from.add_reachable(new Edge(to, time));
        }

        for(ArrayList<Station> stations : db.values()) {
            //환승역의 경우 ArrayList element 2개 이상 생성됨.
            if(stations.size() == 1)
                continue;

            int size = stations.size();
            for(int i = 0; i < size-1; ++i) {
                for(int j = i+1; j < size; ++j) {
                    Station lhs = stations.get(i);
                    Station rhs = stations.get(j);
                    long weight = 5;
                    lhs.add_reachable(new Edge(rhs, weight));
                    rhs.add_reachable(new Edge(lhs, weight));
                    //예를 들어 3개 호선이 맞물린다면, 1-2/1-3/2-3 호선간 환승을 모두 구현
                }
            }
        }

        // 확인해보기. 환승역일 시 본인으로 가는 경로 존재[5]
        int itr1 = 0;
        for(Station s : db.get("서울대")) {
            for(Edge e : s.getReachable()) {
                System.out.println(itr1 + " 서울대 ->" + e.getDest().getName());
            }
            itr1++;
        }

        return db;
    }

    public static void navigate(String from, String to) {
        ArrayList<Station> start = db.get(from);    //시작하는 역. 환승역일 시 정보 여러개
        ArrayList<Station> end = db.get(to);        // 끝나는 역. 환승역일 시 정보 여러개
        int start_transfer_size = start.size();
        int end_transfer_size = end.size();

        String[] res = new String[start_transfer_size*end_transfer_size];
        System.out.println("시작역 환승 개수: " + start_transfer_size);
        System.out.println("끝역 환승 개수: " + end_transfer_size);

        TrackAndDistance temp_result = dijkstra(start.get(0), end.get(0));

        for(int i = 0; i < start_transfer_size; ++i) {
            for(int j = 0; j < end_transfer_size; ++j) {
                if(i == 0 && j == 0)
                    continue;
                initNodes(db);  //db안의 모든 node를 init해준다
                TrackAndDistance cmp_result = dijkstra(start.get(i), end.get(j));
                System.out.println("Temp 거리 = " + temp_result.distance + " Cmp 거리 = " + cmp_result.distance);
                if(cmp_result.distance < temp_result.distance)
                    temp_result = cmp_result;
            }
        }

        StringBuilder sb = new StringBuilder();
        Stack<String> transfer_check = new Stack<>();
        int tempSize = temp_result.track.size();
        transfer_check.push(temp_result.track.get(0).getName());

        boolean diff = true;
        for(int i = 1; i < tempSize; ++i) {
            if(!transfer_check.peek().equals(temp_result.track.get(i).getName())) {
                System.out.println("here1");

                if(diff = false) {
                    StringBuilder build = new StringBuilder();
                    build.append('[');
                    String s = transfer_check.pop();
                    build.append(s);
                    build.append(']');
                    String a = build.toString();
                    transfer_check.push(a);
                    diff = true;
                }else {
                    transfer_check.push(temp_result.track.get(i).getName());
                }
            }
            else {
                System.out.println("here2");
                diff = false;
            }
        }

        while(!transfer_check.isEmpty())
            sb.append(transfer_check.pop() + " ");

        String result = sb.toString().trim();
        System.out.println(result);
        System.out.println(temp_result.distance);
    }

    static class TrackAndDistance {
        private ArrayList<Station> track;
        private long distance;

        public TrackAndDistance(ArrayList<Station> t, long d) {
            track = t;
            distance = d;
        }
    }

    public static TrackAndDistance dijkstra(Station start, Station end) {
        start.visited = true;
        start.distance = 0;
        Station curr = start;
        PriorityQueue<Station> queue = new PriorityQueue<Station>();

        while(curr != end) {
            for(Edge e : curr.getReachable()) {
                if(e.getDest().visited == false) {
                    long accum_distance = curr.distance + e.getWeight();
                    if(accum_distance < e.getDest().distance) {
                        e.getDest().setDistance(accum_distance);
                        e.getDest().track = curr;
                    }
                    queue.offer(e.getDest());
                }
            }

            curr = queue.poll();
            curr.visited = true;
        }

        final long total_distance = curr.distance;
        System.out.println("final d " + total_distance);

        ArrayList<Station> a = new ArrayList<>();
        while(curr != null) {
            a.add(curr);
            curr = curr.track;
        }

        for(Station s : a) {
            System.out.print(s.getName() + " <- ");
        }
        System.out.println();

        return new TrackAndDistance(a, total_distance);

//        while(curr != end) {
//            if(curr.visited == true) {
//                System.out.println("이미 방문 " + curr.getName());
//                continue;
//            }else {
//                curr.visited = true;
//                System.out.println("첫방문 " + curr.getName());
//                System.out.println("트랙에 " +curr.getName()+"을 더합니다" );
//                track.add(curr);    //track에 curr 추가
//
//                boolean flag_for_track = false; //한 곳이라도 갈 곳이 있다면 keep
//
//                for(Edge e : curr.getReachable()) {
//                    // 이미 방문된 엣지의 경우
//                    if(e.getDest().visited == true) {
//                        System.out.println(e.getDest().getName()+"는 이미 방문되었습니다.");
//                        continue;
//                    }
//
//                    // 방문되지 않은 엣지의 경우
//                    flag_for_track = true;
//                    long accum_distance = curr.distance + e.getWeight();
//                    if(accum_distance < e.getDest().distance) {
//                        e.getDest().setDistance(accum_distance);
//                    }
//                    System.out.println(e.getDest().getName() + "["+e.getDest().distance + "]");
//
//                    queue.offer(e.getDest());
//                }
//
//                if(!flag_for_track) {
//                    //한 군데도 갈 곳이 없었다면 delete
//                    System.out.println("트랙에서 " + curr.getName() + "을 지웁니다");
//                    track.remove(curr);
//                }
//
//            }
//
//            //본분을 다한 curr 트랙에 저장
//
//            curr = queue.poll();
//            while(queue.contains(curr))
//                queue.remove(curr);
//
//        }
//
//        System.out.println("****목적지에 도착했습니다 " + curr.getName() + "[" + curr.distance + "]******");
//
////        ArrayList<Station> res = new ArrayList<>();
////        while(curr != null) {
////            res.add(curr);
////            curr = curr.track;
////        }
//        System.out.println("트랙을 인출합니다");
//        for(Station s : track)
//            System.out.print(s.getName() + "->");
//
//        return track;
    }

    public static void initNodes(HashMap<String, ArrayList<Station>> data) {
        Iterator iterator = data.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry a = (Map.Entry) iterator.next();
            String tmp_key = (String)a.getKey();
            ArrayList<Station> tmp_val = (ArrayList<Station>)a.getValue();

            for(Station s : tmp_val) {
                s.init();
            }

            db.put(tmp_key, tmp_val);

        }
    }


 }