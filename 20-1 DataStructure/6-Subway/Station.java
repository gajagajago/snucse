import java.util.LinkedList;

public class Station {
    private String name;
    private LinkedList<Edge> reachable;

    public Station(String s) {
        name = s;
        reachable = new LinkedList<Edge>();
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
}