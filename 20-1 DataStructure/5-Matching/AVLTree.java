import java.util.ArrayList;

public class AVLTree {

    public Node root;
    public ArrayList<Index> searchedIndex;

    // Get total height of a node
    private int height(Node n) {
        if(n == null)
            return 0;

        return Math.max(n.getL_height(), n.getR_height()) + 1;
    }

    private int getBalance(Node n) {
        if(n == null)
            return 0;

        return n.getL_height() - n.getR_height();
    }

    private Node rightRotate(Node root) {
        Node newParentNode = root.getL_child();
        Node predecessor = newParentNode.getR_child();

        newParentNode.setR_child(root);
        root.setL_child(predecessor);

        root.setL_height(height(root.getL_child()));
        root.setR_height(height(root.getR_child()));
        newParentNode.setL_height(height(newParentNode.getL_child()));
        newParentNode.setR_height(height(newParentNode.getR_child()));

        return newParentNode;
    }

    private Node leftRotate(Node root) {
        Node newParentNode = root.getR_child();
        Node inOrderSuccessor = newParentNode.getL_child();

        newParentNode.setL_child(root);
        root.setR_child(inOrderSuccessor);

        root.setL_height(height(root.getL_child()));
        root.setR_height(height(root.getR_child()));
        newParentNode.setL_height(height(newParentNode.getL_child()));
        newParentNode.setR_height(height(newParentNode.getR_child()));

        return newParentNode;
    }

    public Node insert(Node n, String key, Index idx) {
        // Case 1 : Root is empty
        if (n == null)
            return (new Node(key, idx));

        // Case 2 : Root is not empty
        int cmp = key.compareTo(n.getKey());

        // Normal BST insertion & Updating heights
        if (cmp < 0)
        {
            n.setL_child(insert(n.getL_child(), key, idx));
            n.setL_height(height(n.getL_child()));
        }
        else if (cmp > 0)
        {
            n.setR_child(insert(n.getR_child(), key, idx));
            n.setR_height(height(n.getR_child()));
        }
        else { n.addIdx(idx); }

        // Check balance
        int balance = getBalance(n);

        // Right rotation
        if((balance > 1) && (key.compareTo(n.getL_child().getKey()) < 0))
            return rightRotate(n);

        // Left rotation
        if((balance < -1) && (key.compareTo(n.getR_child().getKey()) > 0))
            return leftRotate(n);

        // Left - Right rotation
        if(balance > 1 && key.compareTo(n.getL_child().getKey()) > 0) {
            n.setL_child(leftRotate(n.getL_child()));
            return rightRotate(n);
        }

        // Right - Left rotation
        if(balance < -1 && key.compareTo(n.getR_child().getKey()) < 0) {
            n.setR_child(rightRotate(n.getR_child()));
            return leftRotate(n);
        }

        return n;
    }

    private void preOrder(Node node) {
        if (node != null) {
            System.out.print(node.getKey() + " ");
            preOrder(node.getL_child());
            preOrder(node.getR_child());
        }
    }

    public void searchForKey(Node node, String content) {
        if (node != null) {
            if(node.getKey().compareTo(content) == 0) {
                searchedIndex = new ArrayList<>(node.getIdx());
                return;
            }
            searchForKey(node.getL_child(), content);
            searchForKey(node.getR_child(), content);
        }
    }

    public boolean isEmpty() { return root == null; }

    public void print() {
        if (isEmpty())
            return;
        else preOrder(root);
    }
}

