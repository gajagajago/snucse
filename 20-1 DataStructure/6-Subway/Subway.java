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
            String route = navigate(input[0], input[1]);

            System.out.println(route);
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

    public static String navigate(String from, String to) {
        ArrayList<Station> start = db.get(from);    //시작하는 역. 환승역일 시 정보 여러개
        ArrayList<Station> end = db.get(to);        // 끝나는 역. 환승역일 시 정보 여러개
        int start_transfer_size = start.size();
        int end_transfer_size = end.size();

        String[] res = new String[start_transfer_size*end_transfer_size];
        System.out.println("시작역 환승 개수: " + start_transfer_size);
        System.out.println("끝역 환승 개수: " + end_transfer_size);

        int m = 0;
        for(int i = 0; i < start_transfer_size; ++i) {
            for(int j = 0; j < end_transfer_size; ++j) {
                res[m++] = dijkstra(start.get(i), end.get(j));
                System.out.println(res[m]);
            }
        }



        /* start, end는 처음 입력받았을 때는 자기자신만 들어있다.
        for(Station s1 : start)
            System.out.println(s1.getName());

        for(Station s1 : end)
            System.out.println(s1.getName());
        */


//        ArrayList<Station> tracks = dijkstra(start, end);
//
//        StringBuilder sb = new StringBuilder();
//        for(Station s : tracks) {
//            sb.append(s.getName() + " ");
//        }
//
//        return sb.toString();
        return "PROCESS END";
    }

    public static String dijkstra(Station start, Station end) {
        start.visited = true;
        start.distance = 0;
        Station curr = start;

        PriorityQueue<Station> queue = new PriorityQueue<Station>();
        //트랙킹
        ArrayList<Station> tracking = new ArrayList<>();

        while(curr != end) {
            System.out.println("=====" + curr.getName() + "======");

            for(Edge e : curr.getReachable()) {
                if(e.getDest().visited == true) {
                    System.out.println(e.getDest().getName()+"는 이미 방문되었습니다.");
                    continue;
                }

                long accum_distance = curr.distance + e.getWeight();
                if(accum_distance < e.getDest().distance) {
                    e.getDest().setDistance(accum_distance);
                }
                System.out.println(e.getDest().getName() + "["+e.getDest().distance + "]");

                queue.offer(e.getDest());
            }
            //본분을 다한 curr 트랙에 저장
            tracking.add(curr);
            
            curr = queue.poll();
            curr.visited = true;

            while(queue.contains(curr))
                queue.remove(curr);

        }

        System.out.println("Loop is over " + curr.getName() + "[" + curr.distance + "]");

        StringBuilder sb = new StringBuilder();
        for(Station s : tracking)
            sb.append(s.getName()  + " ");

        System.out.println(sb.toString());

        return sb.toString();
    }


 }