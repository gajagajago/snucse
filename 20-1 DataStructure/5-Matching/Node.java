import java.util.ArrayList;

public class Node implements Comparable<Node> {

    private String key;
    private ArrayList<Index> idx = new ArrayList<>();
    private Node l_child, r_child;
    private int l_height, r_height;

    public Node(String key, Index idx) {
        this.key = key;
        this.idx.add(idx);
        l_child = r_child = null;
        l_height = r_height = 0;
    }

    public String getKey() { return key; }

    public ArrayList<Index> getIdx() { return idx; }

    public Node getL_child() { return l_child; }

    public Node getR_child() { return r_child; }

    public int getL_height() { return l_height; }

    public int getR_height() { return r_height; }

    public void addIdx(Index idx) { this.idx.add(idx); }

    public void setL_child(Node l_child) { this.l_child = l_child; }

    public void setR_child(Node r_child) { this.r_child = r_child; }

    public void setL_height(int l_height) { this.l_height = l_height; }

    public void setR_height(int r_height) { this.r_height = r_height; }

    @Override
    public int compareTo(Node o) { return key.compareTo(o.key); }
}