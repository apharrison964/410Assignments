package assignment1;

public class Node<T> {
	
	T item;
	Node<T> next;
	
	public Node(T x, Node<T> n) {
		item = x;
		next = n;
	}
	
}
