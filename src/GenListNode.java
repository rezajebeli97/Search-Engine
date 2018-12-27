public class GenListNode {
	private String key;
	private LinkList Dlink;
	private GenListNode RightNode;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public GenListNode getRightNode() {
		return RightNode;
	}
	public void setRightNode(GenListNode rightNode) {
		RightNode = rightNode;
	}
	public LinkList getDlink() {
		return Dlink;
	}
	public void setDlink(LinkList dlink) {
		Dlink = dlink;
	}
	
	
}