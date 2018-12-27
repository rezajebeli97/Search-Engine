public class TSTNode implements Node{
	private TSTNode LeftChild;
	private TSTNode MiddleChild;
	private TSTNode RightChild;
	private int bF;
	private int height;
	private LinkList linkList;
	
	private char key;
	
	private boolean isEnd;

	public char getKey() {
		return key;
	}

	public void setKey(char key) {
		this.key = key;
	}

	public TSTNode getLeftChild() {
		return LeftChild;
	}

	public void setLeftChild(TSTNode leftChild) {
		LeftChild = leftChild;
	}

	public TSTNode getRightChild() {
		return RightChild;
	}

	public void setRightChild(TSTNode rightChild) {
		RightChild = rightChild;
	}

	public TSTNode getMiddleChild() {
		return MiddleChild;
	}

	public void setMiddleChild(TSTNode middleChild) {
		MiddleChild = middleChild;
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

	public LinkList getLinkList() {
		return linkList;
	}

	public void setLinkList(LinkList linkList) {
		this.linkList = linkList;
	}
	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getLeftHeight() {
		if (this.getLeftChild() != null) {
			return this.getLeftChild().getHeight();
		} 
		else
			return 0;
	}

	public int getRightHeight() {
		if (this.getRightChild() != null) {
			return this.getRightChild().getHeight();
		} 
		else
			return 0;
	}

	public int getBF() {
		return bF;
	}

	public void setBF(int bF) {
		this.bF = bF;
	}
}
