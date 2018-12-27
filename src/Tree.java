import java.io.File;

public interface Tree {
	public Node root =null;
	public String build(File StopWordFile ,File myFile);
	public String add(File AddFile);
	public String delete(File DeleteFile);
	public String update(File updateFile);
	public String search(String myWord);
	public String multipleSearch(String myWord);
	public String list_l();
	public int peymayesh();
}