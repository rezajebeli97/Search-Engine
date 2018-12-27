public class Stack {
	private int top;
	private String[] A;
	private int maxSize = 1000;

	public Stack() {
		setTop(-1);
		A = new String[maxSize];
	}

	public void push(String x) {
		if (!isFull()) {
			A[setTop(getTop() + 1)] = x;
		}
	}

	public String pop() {
		if (!isEmpty()) {
			return A[top--];
		} else
			return (String) null;
	}

	public boolean isEmpty() {
		if (getTop() == -1)
			return true;
		else
			return false;
	}

	public boolean isFull() {
		if (getTop() == maxSize - 1)
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