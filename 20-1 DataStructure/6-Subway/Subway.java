import java.io.*;
import java.util.*;
import java.nio.file.*;
import static java.nio.charset.Charset.forName;

public class Subway{
    private static HashMap<String, ArrayList<Station>> db;
    private static HashMap<String, Station> id_db;

    public static void main(String args[]) {
        if(args.length != 1) {
            System.err.println("지하철 데이터 파일을 Command line에 입력하십시오.");
            System.exit(1);
        }

        try {
            Path path = Paths.get(args[0]);
            db = build_map(path);
        } catch (IOException e) {
            System.err.println("올바른 지하철 데이터 파일이 아닙니다");
            System.exit(1);
        }

        while(true) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            final String line;

            try {
                line = br.readLine();
                if(line == null || line.equals("QUIT"))
                    break;

                String[] input = line.split(" ");
                initNodes(db);
                navigate(input[0], input[1]);

            } catch (IOException e) {
                System.err.println("입력을 읽던 중 에러가 발생했습니다.");
                System.exit(1);
            } catch (IllegalArgumentException e) {
                System.err.println("올바르지 않은 입력입니다");
                continue;
            }
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

            ArrayList<Station> linkedLines = db.get(name);
            if(linkedLines == null) {
                linkedLines = new ArrayList<Station>();
                db.put(name, linkedLines);
            }
            linkedLines.add(station);

            id_db.put(terms[0], station);
        }

        while(itr.hasNext()) {
            String time_info = itr.next();
            String[] terms = time_info.split(" ");
            String from_id = terms[0], to_id = terms[1];
            long takes = Long.parseLong(terms[2]);

            Station from = id_db.get(from_id);
            Station to = id_db.get(to_id);
            //각 역의 호선별로 reachable한 정보 저장
            from.add_reachable(new Edge(to, takes));
        }

        for(ArrayList<Station> stations : db.values()) {
            // 환승역 처리
            if(stations.size() != 1) {
                int size = stations.size();
                long weight = 5;
                for(int i = 0; i < size-1; ++i) {
                    for(int j = i+1; j < size; ++j) {
                        Station lhs = stations.get(i);
                        Station rhs = stations.get(j);
                        lhs.add_reachable(new Edge(rhs, weight));
                        rhs.add_reachable(new Edge(lhs, weight));
                    }
                }
            }
        }

        return db;
    }

    public static void navigate(String from, String to) {
        ArrayList<Station> start = db.get(from);    //시작하는 역. 환승역일 시 정보 여러개
        ArrayList<Station> end = db.get(to);        // 끝나는 역. 환승역일 시 정보 여러개
        int start_transfer_size = start.size();
        int end_transfer_size = end.size();

        TrackAndDistance temp_result = dijkstra(start.get(0), end.get(0));

        for(int i = 0; i < start_transfer_size; ++i) {
            for(int j = 0; j < end_transfer_size; ++j) {
                if(i == 0 && j == 0)
                    continue;
                initNodes(db);  //db안의 모든 node를 init해준다
                TrackAndDistance cmp_result = dijkstra(start.get(i), end.get(j));
                if(cmp_result.distance < temp_result.distance)
                    temp_result = cmp_result;
            }
        }

        Station last = temp_result.track.get(temp_result.track.size()-1);
        for(int i = temp_result.track.size()-2; i >= 0; --i) {
            if(temp_result.track.get(i).getName().equals(last.getName())) {
                System.out.print("[" + last.getName() + "] ");
                i--;
            }else {
                System.out.print(last.getName() + " ");
            }
            last = temp_result.track.get(i);
        }
        System.out.println(last.getName());
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

        ArrayList<Station> a = new ArrayList<>();
        while(curr != null) {
            a.add(curr);
            curr = curr.track;
        }

        return new TrackAndDistance(a, total_distance);
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