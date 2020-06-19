public class Edge{
    Station dest;
    long weight;

    public Edge(Station d, long w) {
        dest = d;
        weight = w;
    }

    public Station getDest() {
        return dest;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long w) {
        weight = w;
    }
}