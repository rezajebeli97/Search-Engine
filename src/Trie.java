import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Trie implements Tree {

	private TrieNode root;
	private TrieNode StopWordRoot;
	public ArrayList<String> myFiles;
	private ArrayList<StringAndName> myWholeFiles = new ArrayList<StringAndName>();
	private String s,sCheck;
	private int counter=0;
	private boolean needToDelNode = false;
	
	/* Constructor */
	public Trie() {
		root = new  TrieNode('_');
		StopWordRoot = new TrieNode('_');
		myFiles = new ArrayList<String>();
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public String build(File StopWordFile ,File myFile) {

		Scanner scannerStopWords = null;
		try {
			scannerStopWords = new Scanner(StopWordFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (scannerStopWords.hasNextLine()) {
			String myStopWord = scannerStopWords.nextLine();
			String[] stopWords = myStopWord.split("[^a-zA-Z]+");
			for (String string : stopWords) {
				string = string.toLowerCase();
				addKey(StopWordRoot, string, StopWordFile , 0);
			}
		}

		
		
		counter=0;
		
		long start2 =System.currentTimeMillis();
		
		for (File file : myFile.listFiles()) {
			myFiles.add(file.getName()); 	// array of my files

			Scanner scanner = null;
			try {
				scanner = new Scanner(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			StringAndName myFileString = new StringAndName();
			myFileString.namePart = file.getName();
			myFileString.filePart = "";
			
			int indexInFile = 1;
			while (scanner.hasNext()) {
				String myWord = scanner.next();
				String[] words = myWord.split("[^a-zA-Z]+");

				for (String string : words) {
					indexInFile++;
					string = string.toLowerCase();
					myFileString.filePart += string + " ";
					if (!string.equals("") && !findKey(StopWordRoot, string)) 		// check if the word doesn't be a stopWord if(findKey(StopWordRoot , myWord))
						addKey(root, string, file , indexInFile);
				}

			}
			myWholeFiles.add(myFileString);
		}
		
		long finish2 = System.currentTimeMillis();
		return "Trie : " + (finish2-start2) + "ms\nNumber Of Words : " + counter + "\nNumber Of listed files : " + myFiles.size();
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public String add(File AddFile) {
		for (String string : myFiles) {
			if (string.equals(AddFile.getName())) {
				return "err: already exists, you may want to update.";	 			// return , if(!flag) isn't necessary
			}
		}
		if(!AddFile.exists())
			return "err: document not found.";

		myFiles.add(AddFile.getName());
		StringAndName stringAndname = new StringAndName();
		stringAndname.namePart = AddFile.getName();
		stringAndname.filePart = "";
		
		Scanner scannerAdd = null;

		try {
			scannerAdd = new Scanner(AddFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int indexInFile = 1;
		while (scannerAdd.hasNext()) {
			String myWord = scannerAdd.next();
			String[] words = myWord.split("[^a-zA-Z]+");
			for (String string : words) {
				indexInFile++;
				string = string.toLowerCase();
				stringAndname.filePart += string + " ";
				if (!string.equals("") && !findKey(StopWordRoot, string))    // check if the word doesn't be a stopWord if(findKey(StopWordRoot , myWord))
					addKey(root, string, AddFile , indexInFile);
			}
		}
		myWholeFiles.add(stringAndname);	
		return AddFile.getName()+" successfully added.";
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public String delete(File DeleteFile) {
		boolean flag = false;
		for (String string : myFiles) {
			if (string.equals(DeleteFile.getName())) {
				flag = true;
				myFiles.remove(string);
				break;
			} 						// else return , if(flag) isn't necessary //impossible
		}
		

		if (flag) {

			Scanner scannerDelete = null;
			StringAndName stringAndname = null;
			for (int i=0 ; i<myWholeFiles.size() ; i++) {
				stringAndname = myWholeFiles.get(i);
				if(DeleteFile.getName().equals(stringAndname.namePart)){
					scannerDelete = new Scanner(stringAndname.filePart);
					break;
				}	
			}

			while (scannerDelete.hasNext()) {
				String myWord = scannerDelete.next();
				String[] words = myWord.split("[^a-zA-Z]+");
				for (String string : words) {
					string = string.toLowerCase();
					if (!string.equals("") && !findKey(StopWordRoot, string )){ 			// check if the word doesn't be a stopWord if(findKey(StopWordRoot , myWord))
						needToDelNode = false;
						deleteKey(root, string , DeleteFile);
					}
				}
			}
			myWholeFiles.remove(stringAndname);
			return DeleteFile.getName()+" successfully removed from lists.";
		}
		else
			return "err: document not found.";
		
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void deleteKey(TrieNode p, String myWord, File myFile) {
		StackTreeNode stack = new StackTreeNode();
		
		for (char c : myWord.toCharArray()) {
			TrieNode child = p.hisChild(c);
			if (child.getNumberOfChilds() == 1) {
				p.getChildList().remove(child);
				return;
			} else {
				child.setNumberOfChilds(child.getNumberOfChilds() - 1);
				p = child;
				stack.push(p);
			}
		}

		deleteFromLinkListAndIfRootNullDeleteNode(p, myFile);
		
		if(needToDelNode){
			while(!stack.isEmpty() && needToDelNode){
				TrieNode x = (TrieNode) stack.pop();
				TrieNode fatherOfx = (TrieNode) stack.give();
				if(fatherOfx.getChildList().size() == 1){
					fatherOfx.getChildList().remove(x);
					fatherOfx.setNumberOfChilds(fatherOfx.getNumberOfChilds() - 1);
				}
				else{
					fatherOfx.getChildList().remove(x);
					fatherOfx.setNumberOfChilds(fatherOfx.getNumberOfChilds() - 1);
					needToDelNode = false;
				}
					
			}
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public String update(File updateFile) {
		String s;
		s = delete(updateFile);
		if(s.equals(updateFile.getName()+" successfully removed from lists.")){
			add(updateFile);
			return updateFile.getName()+" successfully updated.";
		}
		
		else
			return s;
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public String multipleSearch(String myWord){
		myWord = myWord.toLowerCase();
		ArrayList<String> arr= new ArrayList<String>();
		s="";
		String result="";
		String[] words = myWord.split("[^a-zA-Z]+");
		for (String string : words) {
			if(!string.equals("")){
				if(findKey(StopWordRoot, string)){
					continue;
				}
				s="";
				String searchResult = searchKey(root, string);
				if(searchResult.equals(string + " : doesn't exist"))
					return "not found.";
				arr.add(searchResult);
			}
		}
		ArrayList <ArrayList<String>> arr2 = new ArrayList<ArrayList<String>>();
		for (String string : arr) {
			if(!string.equals("")){
				String[] s = string.split("[^a-zA-Z]+");
				arr2.add(new ArrayList<String>((Arrays.asList(s))));
			}
		}
		
		if(arr2.size() == 0)
			return "no word";
		
		if(arr2.size() == 1){
			for (String string : arr2.get(0)) {
				result += string + " ";
			}
			return myWord + " -> " + result;
		}
		
		for (String firstArray : arr2.get(0)) {
			boolean flag = true;
			for(int i=1 ; i<arr2.size() ; i++){
				if(!arr2.get(i).contains(firstArray))
					flag = false;
			}
			if(flag)
				result += firstArray+" ";
		}
		return myWord + " -> " + result;
		
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public String search(String myWord){
		myWord = myWord.toLowerCase();
		s= myWord+" -> ";
		return searchKey(root, myWord);
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public String list_l(){
		counter = 0;
		String myFilesList = "";
		for (String string : myFiles) {
			myFilesList += string +"\n";
			counter++;
		}
		return myFilesList + "Number of listed docs = " + counter + "\n";
}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public int peymayesh() {
		counter = 0;
		peymayeshTrie(root, "");
		return counter;
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void peymayeshTrie(TrieNode p , String myWord ) {
		if(p==null)
			return;
		
		if(p.getChildList().size()>0)
			if(p.getKey()=='_')
				peymayeshTrie(p.getChildList().get(0) , myWord);
			else
				peymayeshTrie(p.getChildList().get(0) , myWord + p.getKey());
		if(p.isEnd()){
			counter++;
			MyFrame.textArea.setText(MyFrame.textArea.getText() + myWord + p.getKey() + " -> ");
			sCheck = "";
			LinkListNode temp=p.getLinkList().getRoot();
			while(temp!=null){
				if(!temp.getKey().equals(sCheck)){
					MyFrame.textArea.setText(MyFrame.textArea.getText() + temp.getKey().replaceAll(".txt", "")+" ");
					sCheck = temp.getKey();
				}
				temp = temp.getRightNode();

			}
			MyFrame.textArea.setText(MyFrame.textArea.getText() + "\n");
		}
		for (int i=1 ; i<p.getChildList().size() ; i++) {
			if(p.getKey() == '_')
				peymayeshTrie(p.getChildList().get(i) , myWord);
			else
				peymayeshTrie(p.getChildList().get(i) , myWord + p.getKey());
		}
		
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private String searchKey(TrieNode p, String myWord) {
		for (char c : myWord.toCharArray()) {
			if (p.hisChild(c) == null)		 // p==null ham ehtiaj nadarad
				return myWord + " : doesn't exist";
			else
				p = p.hisChild(c);
		}
		if (p.isEnd() == true){
			LinkListNode temp=p.getLinkList().getRoot();
			while(temp!=null){
				if(!temp.getKey().equals(sCheck)){
					s += temp.getKey().replaceAll(".txt", "")+" ";
					sCheck = temp.getKey();
				}
				temp = temp.getRightNode();
			}
			return s;
		}
			
		return myWord + " : doesn't exist"; 			// just for ignore java error
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void addKey(TrieNode p, String myWord, File myFile, int indexInFile) {

		for (char c : myWord.toCharArray()) {
			TrieNode child = p.hisChild(c);
			if (child != null)
				p = child;
			else {
				p.getChildList().add(new TrieNode(c));
				p = p.hisChild(c);
			}
			p.setNumberOfChilds(p.getNumberOfChilds() + 1);
		}

		p.setEnd(true);

		if (p.getLinkList() == null) {
			counter++;
			p.setLinkList(new LinkList());
		}
		LinkListNode lln = new LinkListNode();
		lln.setKey(myFile.getName());
		lln.setIndex(indexInFile);
		lln.setRightNode(p.getLinkList().getRoot());
		p.getLinkList().setRoot(lln);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private boolean findKey(TrieNode p, String myWord) {
		for (char c : myWord.toCharArray()) {
			if (p.hisChild(c) == null)		 // p==null ham ehtiaj nadarad
				return false;
			else
				p = p.hisChild(c);
		}
		if (p.isEnd() == true)
			return true;
		return false; 			// just for ignore java error
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void deleteFromLinkListAndIfRootNullDeleteNode (TrieNode p /*, TSTNode fatherOfp , boolean isRightChild*/, File myFile ) {
		LinkListNode temp, preTemp = null;
		temp = p.getLinkList().getRoot();
		while (temp != null) {
			if ( temp.getKey().equals(myFile.getName())) {
		
					if (preTemp == null) { 														// momkene esme file ii ke donbaleshim avalin node az linklistemoon bashe
						p.getLinkList().setRoot(temp.getRightNode());
						temp = temp.getRightNode();
					} else {
						preTemp.setRightNode(temp.getRightNode());
						temp = temp.getRightNode();
					}
		
				if(p.getLinkList().getRoot()==null){
				//	p.setLinkList(null);
					p.setEnd(false);
					if(p.getChildList().size()==0)
						needToDelNode = true;
				}
				break;
			}
			preTemp = temp;
			temp = temp.getRightNode();
		}
		
	}
	
	
}