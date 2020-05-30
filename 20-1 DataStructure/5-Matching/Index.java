public class Index {

    private int line;
    private int row;

    public Index(int line, int row) {
        this.line = line;
        this.row = row;
    }

    public int getLine() { return line; }

    public int getRow() { return row; }

    public String print() { return ("(" + line + ", " + row + ")"); }
}