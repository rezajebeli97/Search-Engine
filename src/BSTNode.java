public class BSTNode implements Node {
	private BSTNode LeftChild;
	private BSTNode RightChild;
	private int bF;
	private int height;
	private LinkList linkList;
	private String key;

	public BSTNode(String s) {
		setKey(s);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public BSTNode getLeftChild() {
			return LeftChild;
	}

	public void setLeftChild(BSTNode leftChild) {
			LeftChild = leftChild;
	}

	public BSTNode getRightChild() {
		return RightChild;
	}

	public void setRightChild(BSTNode rightChild) {
		RightChild = rightChild;
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