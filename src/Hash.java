import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Hash {
	private int counter;
	private GenList[] HashTable;
	private GenList[] StopWordsHashTable;
	private int maxSize = 100000;
	public ArrayList<String> myFiles;
	private ArrayList<StringAndName> myWholeFiles = new ArrayList<StringAndName>();

	public Hash() {
		HashTable = new GenList[maxSize];
		StopWordsHashTable = new GenList[maxSize];
		myFiles = new ArrayList<String>();
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public String build(File StopWordFile, File myFile) {

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
				addKey(StopWordsHashTable, string, StopWordFile, 0);
			}
		}

		counter = 0;
		long start2 = System.currentTimeMillis();

		for (File file : myFile.listFiles()) {
			myFiles.add(file.getName()); // array of my files

			Scanner scanner = null;
			try {
				scanner = new Scanner(file);
			} catch (FileNotFoundException e) {
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
					if (!string.equals("") && !findKey(StopWordsHashTable, string)) { // check
																						// if
																						// the
																						// word
																						// doesn't
																						// be
																						// a
																						// stopWord
						addKey(HashTable, string, file, indexInFile);
					}
				}
			}
			myWholeFiles.add(myFileString);
		}

		long finish2 = System.currentTimeMillis();
		return "Hash : " + (finish2 - start2) + "ms\nNumber Of Words : " + counter + "\nNumber Of listed files : "
				+ myFiles.size();
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public String add(File AddFile) {
		for (String string : myFiles) {
			if (string.equals(AddFile.getName())) {
				return "err: already exists, you may want to update."; 			// return , if(!flag) isn't necessary
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
				if (!string.equals("") && !findKey(StopWordsHashTable, string)){    // check if the word doesn't be a stopWord if(findKey(StopWordRoot , myWord))
					addKey(HashTable ,  string , AddFile , indexInFile );
				}
			}
		}
		myWholeFiles.add(stringAndname);
		return AddFile.getName()+" successfully added.";
		
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
					if (!string.equals("") && !findKey(StopWordsHashTable, string)){ 			// check if the word doesn't be a stopWord if(findKey(StopWordRoot , myWord))
				        deleteKey(HashTable , string , DeleteFile);
					}
				}
			}
			myWholeFiles.remove(stringAndname);
			return DeleteFile.getName()+" successfully removed from lists.";
		}
		else
			return "err: document not found.";
		
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

	
	public String multipleSearch(String myWord){
		myWord = myWord.toLowerCase();
		ArrayList<String> arr= new ArrayList<String>();
		String result="";
		String[] words = myWord.split("[^a-zA-Z]+");
		for (String string : words) {
			if(!string.equals("")){
				if(findKey(StopWordsHashTable, string)){
					continue;
				}
				String searchResult = searchKey(HashTable, "", string);
				if(searchResult.equals(string + " : doesn't exist"))
					return "not found.";
				arr.add(searchResult);
			}
		}
		ArrayList <ArrayList<String>> arr2 = new ArrayList<ArrayList<String>>();
		for (String string : arr) {
			if(!string.equals("")){
				String[] s1 = string.split("[^a-zA-Z]+");
				arr2.add(new ArrayList<String>((Arrays.asList(s1))));
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

	public String search(String myWord){
		myWord = myWord.toLowerCase();
		String s = myWord + " -> ";
		return searchKey(HashTable, s , myWord);
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public String list_l(){
		int myCounter = 0;
		String myFilesList = "";
		for (String string : myFiles) {
			myFilesList += string +"\n";
			myCounter++;
		}
		return myFilesList + "Number of listed docs = " + myCounter + "\n" ;
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public int peymayesh(){
		return peymayeshPrivate(HashTable);
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void addKey(GenList[] HT, String myWord, File myFile, int indexInFile) {

		int hashCode = HashFunction(myWord);

		if (HT[hashCode] == null) {
			counter++;
			HT[hashCode] = new GenList();
			GenListNode Gln = new GenListNode();
			Gln.setKey(myWord);
			Gln.setDlink(new LinkList());
			HT[hashCode].setRoot(Gln);
			LinkListNode lln = new LinkListNode();
			lln.setKey(myFile.getName());
			lln.setIndex(indexInFile);
			Gln.getDlink().setRoot(lln);
		}
		else{
			GenListNode p = HT[hashCode].getRoot();
			GenListNode preP = null;
			while(p!=null){
				if(p.getKey().equals(myWord)){
					LinkListNode lln = new LinkListNode();
					lln.setKey(myFile.getName());
					lln.setIndex(indexInFile);
					lln.setRightNode(p.getDlink().getRoot());
					p.getDlink().setRoot(lln);
					return;
				}
				preP = p;
				p=p.getRightNode();
			}
			
			counter++;
			GenListNode Gln = new GenListNode();
			Gln.setKey(myWord);
			Gln.setDlink(new LinkList());
			preP.setRightNode(Gln);
			LinkListNode lln = new LinkListNode();
			lln.setKey(myFile.getName());
			lln.setIndex(indexInFile);
			Gln.getDlink().setRoot(lln);
		}	
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void deleteKey(GenList[] HT, String myWord, File myFile) {

		int hashCode = HashFunction(myWord);

		GenListNode p = HT[hashCode].getRoot();
		GenListNode preP = null;
		while (p != null) {
			if (p.getKey().equals(myWord)){
				LinkListNode temp = p.getDlink().getRoot();
				LinkListNode preTemp = null;
				while(temp!=null){
					if(temp.getKey().equals(myFile.getName())){
						if (preTemp == null) { 			// momkene esme file ii ke donbaleshim avalin node az linklistemoon bashe
							p.getDlink().setRoot(temp.getRightNode());
						} else {
							preTemp.setRightNode(temp.getRightNode());
						}
						
						break;
					}
					preTemp = temp;
					temp = temp.getRightNode();
				}
				break;
			}
			preP = p;
			p = p.getRightNode();
		}
		
		if(p.getDlink().getRoot() == null){
			if (preP == null) {				 // momkene kalame ii ke donbaleshim avalin node az linklistemoon bashe
				if(p.getRightNode() == null)
					HT[hashCode] = null;			//be jaye inke root null beshe
				else
					HT[hashCode].setRoot(p.getRightNode());
			} else {
				preP.setRightNode(p.getRightNode());
			}
		}
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private boolean findKey(GenList[] HT, String myWord) {

		int hashCode = HashFunction(myWord);
		
		if(HT[hashCode] == null)
			return false;
		
		GenListNode p = HT[hashCode].getRoot();
		while (p != null) {
			if (p.getKey().equals(myWord))
				return true;
			p = p.getRightNode();
		}

		return false;
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private String searchKey(GenList[] HT, String s ,String myWord ) {
		int hashCode = HashFunction(myWord);
		String sCheck="";
		
		if(HT[hashCode] == null)
			return myWord + " : doesn't exist";
		
		GenListNode p = HT[hashCode].getRoot();
		
		while (p != null) {
			if (p.getKey().equals(myWord)){
				LinkListNode temp = p.getDlink().getRoot();
				
				while(temp!=null){
					if(!temp.getKey().equals(sCheck)){
						s += temp.getKey().replaceAll(".txt", "")+" ";
						sCheck = temp.getKey();
					}
					temp = temp.getRightNode();
				}
				return s;
			}
			p = p.getRightNode();
		}

		return myWord + " : doesn't exist";
	}

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private int peymayeshPrivate (GenList[] HT){
		int counter=0;
		String sCheck="";
		
		for (GenList gl : HT) {
			if(gl == null)
				continue;
				
			if(gl.getRoot() == null){									//mahze ehtiat
				System.out.println("Err: root is null but my hashTable isn't null :/ ");
				continue;
			}
			
			GenListNode p = gl.getRoot();
			
			while(p!=null){
				counter++;
				MyFrame.textArea.setText(MyFrame.textArea.getText() + p.getKey() + " -> ");
				sCheck = "";
				LinkListNode temp = p.getDlink().getRoot();
				while(temp!=null){
					if(!temp.getKey().equals(sCheck)){
						MyFrame.textArea.setText(MyFrame.textArea.getText() + temp.getKey().replaceAll(".txt", "") + " " + temp.getIndex()+"   ");
						sCheck = temp.getKey();
					}
					temp = temp.getRightNode();
				}
				MyFrame.textArea.setText(MyFrame.textArea.getText() + "\n");
				p=p.getRightNode();
			}
						
		}
		return counter;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private int HashFunction(String myWord) {
		int hashCode = 0;
		for(int i =0 ; i<myWord.length() ; i++)
			hashCode += myWord.charAt(i);
		return Math.abs(hashCode % maxSize);
		//return Math.abs(myWord.hashCode() % maxSize);
	}
}