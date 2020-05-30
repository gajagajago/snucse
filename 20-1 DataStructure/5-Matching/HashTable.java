import java.util.ArrayList;

public class HashTable {

    private AVLTree[] table;

    //Initialize table w/ slots of empty AVLTree
    public HashTable() {
        table = new AVLTree[Matching.TABLE_SIZE];
        for (int i = 0; i < Matching.TABLE_SIZE; ++i)
            table[i] = new AVLTree();
    }

    //Hash function h(x)
    private static int hash(String s) {
        int hash_val = 0;
        for (int i = 0; i < s.length(); ++i)
            hash_val += (int) s.charAt(i);
        hash_val %= Matching.TABLE_SIZE;

        return hash_val;
    }

    // Command '>' :
    // Convert string into hash -> insert string & position info into certain hash slot
    public void insert(String s, int line, int row) {
        int hash = hash(s);
        Index position = new Index(line, row);
        table[hash].root = table[hash].insert(table[hash].root, s, position);
    }

    // Command '@' : Print keys in certain hash slot
    public void print(int slot_num) {
        if (table[slot_num].isEmpty()) {
            System.out.println("EMPTY");
        } else {
            table[slot_num].print();
            String output = table[slot_num].preOrderList.toString();
            System.out.println(output.substring(0, output.length()-1));
            table[slot_num].initPreOrderList();
        }
    }

    // Command '?' :
    // Convert string into hash -> access certain hash slot & find certain key -> retrieve position info
    public void search(String pattern) {
        // Partition pattern w/ HASH_SIZE
        int quotient = pattern.length() / Matching.HASH_SIZE;
        int remainder = pattern.length() % Matching.HASH_SIZE;

        ArrayList<Index> first = null;

        for (int i = 0; i < quotient; ++i) {
            String part = pattern.substring(i * Matching.HASH_SIZE, (i+1) * Matching.HASH_SIZE);
            int hash = hash(part);
            table[hash].searchForKey(table[hash].root, part);   // sets member var searchedIndex w/ found positions

            if(table[hash].searchedIndex == null) {
                System.out.println("(0, 0)");
                return;
            }

            if(i == 0) {
                first = new ArrayList<>(table[hash].searchedIndex);
                table[hash].initSearchedIndex();
                continue;
            }

            // if (i != 0) make copy of first & compare it w/ current searchedIndex
            ArrayList<Index> copy_first = new ArrayList<>(first);
            ArrayList<Index> temp = new ArrayList<>(table[hash].searchedIndex);

            for(Index a : copy_first) {
                boolean flag = false;
                for(Index b : temp) {
                    if(a.getLine() == b.getLine() && a.getRow() == b.getRow() - Matching.HASH_SIZE*i) {
                        flag = true;
                        break;
                    }
                }
                if(!flag)
                    first.remove(a);
            }

            table[hash].initSearchedIndex();
        }

        if(remainder != 0) {
            String part = pattern.substring(pattern.length() - Matching.HASH_SIZE);
            int hash = hash(part);
            table[hash].searchForKey(table[hash].root, part);   // sets member var searchedIndex w/ found positions

            if(table[hash].searchedIndex == null) {
                System.out.println("(0, 0)");
                return;
            }

            ArrayList<Index> copy_first = new ArrayList<>(first);
            ArrayList<Index> temp = new ArrayList<>(table[hash].searchedIndex);

            for(Index a : copy_first) {
                boolean flag = false;
                for(Index b : temp) {
                    if(a.getLine() == b.getLine() && a.getRow() == b.getRow() + Matching.HASH_SIZE - pattern.length()) {
                        flag = true;
                        break;
                    }
                }
                if(!flag)
                    first.remove(a);
            }

            table[hash].initSearchedIndex();
        }

        if(first.isEmpty()) {
            System.out.println("(0, 0)");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for(Index a : first) {
            sb.append(a.print());
            sb.append(' ');
        }
        System.out.println(sb.toString().trim());
    }
}