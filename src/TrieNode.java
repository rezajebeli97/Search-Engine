import java.util.ArrayList;
import java.util.LinkedList;

public class TrieNode implements Node{
    private char key;
    private boolean isEnd; 
    private int numberOfChilds;
    private ArrayList<TrieNode> childList;
    private LinkList linkList;
    
    /* Constructor */
    public TrieNode(char c){
        setChildList(new ArrayList<TrieNode>());
        setKey(c);
        setNumberOfChilds(0);
    }  
    
    public TrieNode hisChild(char c){
        if (getChildList() != null)
            for (TrieNode eachChild : getChildList())
                if (eachChild.getKey() == c)
                    return eachChild;
        return null;
    }

    public int getNumberOfChilds() {
		return numberOfChilds;
	}

	public void setNumberOfChilds(int numberOfChilds) {
		this.numberOfChilds = numberOfChilds;
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

	public ArrayList<TrieNode> getChildList() {
		return childList;
	}

	public void setChildList(ArrayList<TrieNode> childList) {
		this.childList = childList;
	}

	public char getKey() {
		return key;
	}

	public void setKey(char key) {
		this.key = key;
	}

}