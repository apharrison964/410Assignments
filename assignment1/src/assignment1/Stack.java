package assignment1;

public class Stack<T> {

	Node<T> first;
	int size;

	public Stack() {
		first = null;
		size = 0;
	}

	public boolean isEmpty() {
		return size == 0;

	}

	public T peek() {
		return first.item;
	}

	public T pop() {
		T temp;
		temp = peek();
		first = first.next;
		size--;
		return temp;

	}

	public void push(T element) {
		first = new Node<T>(element, first);
		size++;
	}

}
