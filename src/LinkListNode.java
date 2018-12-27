public class LinkListNode {
	private String key;
	private LinkListNode RightNode;
	private int indexInFile;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public LinkListNode getRightNode() {
		return RightNode;
	}
	public void setRightNode(LinkListNode rightNode) {
		RightNode = rightNode;
	}
	public int getIndex() {
		return indexInFile;
	}
	public void setIndex(int index) {
		this.indexInFile = index;
	}
}