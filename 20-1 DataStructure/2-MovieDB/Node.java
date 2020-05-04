import java.util.NoSuchElementException;

public class Node<T> {
    private T item;
    private Node<T> next;

    public Node(T obj) {
        this.item = obj;
        this.next = null;
    }
    
    public Node(T obj, Node<T> next) {
    	this.item = obj;
    	this.next = next;
    }
    
    public final T getItem() {return item;}
    
    public final void setItem(T item) {this.item = item;}
    
    public Node<T> getNext() {return this.next;}  
    
    public final void insertNext(T obj) {
    	Node<T> curr = this.getNext();
    	Node<T> in = new Node<T>(obj, curr);
    	this.next = in;    	
    }
        
    public boolean hasNext() {return this.getNext() != null;}
    
    public final void removeNext() {
    	Node<T> curr = this.getNext();
    	if(curr == null) {throw new NoSuchElementException();}
    	this.next = curr.getNext();
    }
}