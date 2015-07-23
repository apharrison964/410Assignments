package assignment1;

public class Queue<T> {
	
	Node<T> first;
	Node<T> rear;
	int size;
	
	public Queue() {
		first = null;
		rear = null;
		size = 0;
	}
	
	public boolean isEmpty() {
		return size == 0;
		
	}
	
	public T peek() {
		return first.item;
	}
	
	public T dequeue() {
		T temp = first.item;
		first = first.next;
		size--;
		return temp;
		
	}
	
	public void enqueue(T element) {
		Node<T> temp = new Node<T>(element, null);
		if(size == 0) {
			rear = temp;
			first = temp;
		} else {
			rear.next = temp;
			rear = temp;
			
		}
		
		size++;
	}

}
