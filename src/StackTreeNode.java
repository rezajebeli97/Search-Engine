public class StackTreeNode {
	private int top;
	private Node[] A;
	private int maxSize = 1000;

	public StackTreeNode() {
		top = -1;
		A = new Node[maxSize];
	}

	public void push(Node x) {
		if (!isFull()) {
			A[setTop(getTop() + 1)] = x;
		}
	}
	
	public Node pop() {
		if (!isEmpty()) {
			return A[top--];
		} else
			return (Node) null;
	}

	public Node give() {
		if (!isEmpty()) {
			return A[top];
		} else
			return (Node) null;
	}
	
	public boolean isEmpty() {
		if ( top == -1)
			return true;
		else
			return false;
	}

	public boolean isFull() {
		if ( top == maxSize - 1)
			return true;
		else
			return false;
	}

	public int getTop() {
		return top;
	}

	public int setTop(int top) {
		this.top = top;
		return top;
	}
}