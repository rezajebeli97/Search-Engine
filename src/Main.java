import java.io.File;

public class Main {
	
	public static void main(String[] args) {
		 
		Hash hash = new Hash();
		hash.build(new File("/Users/jebeli/Desktop/StopWords.txt"), new File("/Users/jebeli/Desktop/docs"));
		System.out.println("sdf");
	}
}
