import java.util.LinkedList;

public class Station implements Comparable<Station>{
    private final String name;
    private final LinkedList<Rail> reachable;

    private boolean visited;
    private long distance;
    private Station track;

    public Station(String s) {
        name = s;
        reachable = new LinkedList<Rail>();
        visited = false;
        distance = Integer.MAX_VALUE;
        track = null;
    }

    public void init() {
        visited = false;
        distance = Integer.MAX_VALUE;
        track = null;
    }

    public String getName() {
        return name;
    }

    public LinkedList<Rail> getReachable() {
        return reachable;
    }

    public void add_reachable(Rail e) {
        reachable.add(e);
    }

    public long getDistance() { return distance; }

    public void setDistance(long d) {
        distance = d;
    }

    public boolean getVisited() { return visited; }

    public void setVisited(boolean v) { visited = v; }

    public Station getTrack() { return track; }

    public void setTrack(Station track) { this.track = track; }

    @Override
    public int compareTo(Station o) {
        int cmp = distance <= o.distance ? -1 : 1;
        return cmp;
    }
}