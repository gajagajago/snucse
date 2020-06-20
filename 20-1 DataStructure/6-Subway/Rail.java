public class Rail{
    private Station dest;
    private long weight;

    public Rail(Station d, long w) {
        dest = d;
        weight = w;
    }

    public Station getDest() {
        return dest;
    }

    public long getWeight() {
        return weight;
    }
}