
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<T extends Comparable<T>> implements ListInterface<T> {
	// dummy head
	Node<T> head;
	int numItems;

	public MyLinkedList() {
		head = new Node<T>(null);
		numItems = 0;
	}

    public final Iterator<T> iterator() {return new MyLinkedListIterator<T>(this);}

	@Override
	public boolean isEmpty() {return numItems == 0;}

	@Override
	public int size() {return numItems;}

	@Override
	public void insert(T item) {
		Node<T> point = head;	//insert point
		while(point.hasNext()) {
			int compared = item.compareTo(point.getNext().getItem());
			if(compared < 0) break;		//insert after current point
			if(compared == 0) return;	//skip insert existing item
			point = point.getNext();
		}
		point.insertNext(item);
		numItems ++;
	}
	
	@Override
	public void delete(T item) {
		Node<T> point = head;	//delete point
		while(point.hasNext()) {
			int compared = item.compareTo(point.getNext().getItem());
			if(compared == 0) {	//delete when same
				point.removeNext();
				numItems --;
				return;
			}
			point = point.getNext();
		}
	}
	
	@Override
	public T first() {return head.getNext().getItem();}

	@Override
	public void removeAll() {head.insertNext(null);}
}

class MyLinkedListIterator<T extends Comparable <T>> implements Iterator<T> {
	private MyLinkedList<T> list;
	private Node<T> curr;
	private Node<T> prev;

	public MyLinkedListIterator(MyLinkedList<T> list) {
		this.list = list;
		this.curr = list.head;
		this.prev = null;
	}

	@Override
	public boolean hasNext() {return curr.getNext() != null;}

	@Override
	public T next() {
		if (!hasNext()) {throw new NoSuchElementException();}

		prev = curr;
		curr = curr.getNext();

		return curr.getItem();
	}

	@Override
	public void remove() {
		if (prev == null) {throw new IllegalStateException("next() should be called first");}
		if (curr == null) {throw new NoSuchElementException();}
		
		prev.removeNext();
		list.numItems -= 1;
		curr = prev;
		prev = null;
	}
}