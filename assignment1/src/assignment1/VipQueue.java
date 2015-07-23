package assignment1;

public class VipQueue<T> {
	
	Stack<T> stack = new Stack<T>();
	Queue<T> queue = new Queue<T>();

	public VipQueue() {
		
	}
	
	public boolean isEmpty() {
		return stack.isEmpty() && queue.isEmpty();
	}

	public T peek() {
		if(stack.isEmpty() == false) {
			return stack.peek();
		} else if(queue.isEmpty() == false) {
			return queue.peek();
		} else {
			return null;
		}
	}

	public T dequeue() {
		if(stack.isEmpty() == false) {
			return stack.pop();
		} else
		return queue.dequeue();
	}

	public void enqueue(T element) {
		queue.enqueue(element);
	}

	public void vipEnqueue(T element) {
		stack.push(element);
	}

}
