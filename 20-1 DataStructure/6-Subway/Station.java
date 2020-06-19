import java.util.LinkedList;

public class Station implements Comparable<Station>{
    private String name;
    private LinkedList<Edge> reachable;

    public boolean visited;
    public long distance;

    public Station(String s) {
        name = s;
        reachable = new LinkedList<Edge>();
        visited = false;
        distance = Integer.MAX_VALUE;
    }

    public String getName() {
        return name;
    }

    public LinkedList<Edge> getReachable() {
        return reachable;
    }

    public void add_reachable(Edge e) {
        reachable.add(e);
    }

    @Override
    public int compareTo(Station o) {
        int cmp = distance <= o.distance ? -1 : 1;
        return cmp;
    }
}