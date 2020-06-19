import java.io.*;
import java.util.*;
import java.nio.file.*;
import static java.nio.charset.Charset.forName;

public class Subway{
    public static void main(String args[]) throws FileNotFoundException {
        if(args.length != 1) {
            System.err.println("지하철 데이터 파일을 command line에 입력해주세요.");
            System.exit(1);
        }

        Path path = Paths.get(args[0]);
        HashMap<String, Station> db;

        try {
            db = build_map(path);
        } catch (IOException e) {
            System.err.println("올바르지 않은 데이터 파일입니다.");
            System.exit(1);
        }

    }

    public static HashMap<String, Station> build_map(Path path) throws IOException {
        HashMap<String, Station> db = new HashMap<>();
        HashMap<String, Station> id_db = new HashMap<String, Station>();

        List<String> lines = Files.readAllLines(path, forName("UTF-8"));
        Iterator<String> itr= lines.iterator();

        while(itr.hasNext()) {
            String station_info = itr.next();
            if("".equals(station_info))
                break;

            String[] terms = station_info.split(" ");
            String id = terms[0], name = terms[1];

            Station station = new Station(name);

            ArrayList<Station> near = new ArrayList<Station>();
            near.add(station);

            db.put(name, station);
            id_db.put(id, station);
        }

        //Test
//        System.out.println(db.keySet());
//        System.out.println(id_db.keySet());

        while(itr.hasNext()) {
            String time_info = itr.next();
            String[] terms = time_info.split(" ");
            String from_id = terms[0], to_id = terms[1];
            long time = Long.parseLong(terms[2]);

            String from_name = id_db.get(from_id).getName();

            Station from = db.get(from_name);
            Station to = id_db.get(to_id);

            from.add_reachable(new Edge(to, time));
        }

        Station b = db.get("강남");
        LinkedList<Edge> r = b.getReachable();
        for(Edge e : r) {
            System.out.print(e.getDest().getName() + " ");
            System.out.println(e.getWeight());
        }
        
        return db;
    }
}