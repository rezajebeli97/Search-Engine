import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class BST implements Tree{
	private BSTNode root;
	private BSTNode StopWordRoot;
	public ArrayList<String> myFiles;
	private ArrayList<StringAndName> myWholeFiles = new ArrayList<StringAndName>();
	private String s,sCheck;
	private int counter;
	private boolean isRepeated = false , needToCheckAVL = false;
	StackTreeNode stackDel = new StackTreeNode();
	
	/* Constructor */
	public BST() {
		root = new BSTNode("");
		StopWordRoot = new BSTNode("");
		myFiles = new ArrayList<String>();
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	@Override
	public String build(File StopWordFile , File myFile) {
		
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
				isRepeated = false;
				addKey(StopWordRoot, null , string, StopWordFile , 0 , true );
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
					if (!string.equals("") && !findKey(StopWordRoot, string)) {		// check if the word doesn't be a stopWord if(findKey(StopWordRoot , myWord))
						isRepeated = false;
						addKey(root, null , string, file , indexInFile , false);
					}
				}
			}
			myWholeFiles.add(myFileString);
		}
		
		long finish2 = System.currentTimeMillis();
		return "BST : " + (finish2-start2) + "ms\nNumber Of Words : " + counter + "\nHeight of AVL tree : " + root.getHeight() + "\nNumber Of listed files : " + myFiles.size();
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
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
				if (!string.equals("") && !findKey(StopWordRoot, string)){    // check if the word doesn't be a stopWord if(findKey(StopWordRoot , myWord))
					isRepeated = false;
					addKey(root , null , string, AddFile , indexInFile , false);
				}
			}
		}
		myWholeFiles.add(stringAndname);	
		return AddFile.getName()+" successfully added.";
		
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
					if (!string.equals("") && !findKey(StopWordRoot, string)){ 			// check if the word doesn't be a stopWord if(findKey(StopWordRoot , myWord))
						BSTNode fatherOfRoot = new BSTNode("");
				        fatherOfRoot.setRightChild(root);
				        boolean isRightChild = true;
				        needToCheckAVL = false;
				        deleteKey(root, fatherOfRoot , string , isRightChild , DeleteFile);
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
	
	private void deleteKey(BSTNode p, BSTNode fatherOfp, String myWord, boolean isRightChild, File myFile){
		
		
		if(myWord.compareTo(p.getKey()) == 0){					//myWord==p.key  	hazf az linklist
			deleteFromLinkListAndIfRootNullDeleteNode(p, fatherOfp, isRightChild, myFile);
		}
		
		else if(myWord.compareTo(p.getKey()) < 0){				//myWord<p.key
			deleteKey(p.getLeftChild() , p , myWord , false , myFile);
			if(needToCheckAVL){
				p.setHeight(Math.max( p.getLeftHeight() , p.getRightHeight()) + 1 );
				p.setBF( p.getLeftHeight() - p.getRightHeight() );
				balance(p, fatherOfp, false);
			}
		}
		else {													//myWord>p.key
			deleteKey(p.getRightChild() , p , myWord , true , myFile);
			if(needToCheckAVL){
				p.setHeight(Math.max( p.getLeftHeight() , p.getRightHeight()) + 1 );
				p.setBF( p.getLeftHeight() - p.getRightHeight() );
				balance(p, fatherOfp, false);
			}
		}
		
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
		sCheck = null;
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
		sCheck = null;
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
		return myFilesList + "Number of listed docs = " + counter + "\n" ;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public int peymayesh() {
		counter = 0;
		inOrder(root);
		return counter;
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void inOrder( BSTNode p ){
		if(p==null)
			return;
		inOrder(p.getLeftChild());
		if(p.getLinkList().getRoot()!=null){						//because bst delete isn't complete 
			counter++;	
			MyFrame.textArea.setText(MyFrame.textArea.getText() + p.getKey() + " -> ");
		
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
		inOrder(p.getRightChild());
		
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private String searchKey(BSTNode p , String myWord ){
		if (p.getKey() == "")
			return myWord + " : doesn't exist";
		
		if(myWord.compareTo(p.getKey()) == 0){					//myWord==p.key  	sharte khoorooj
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
		
		else if(myWord.compareTo(p.getKey()) < 0){				//myWord<p.key
			if(p.getLeftChild()==null)
				return myWord + " : doesn't exist";
			return searchKey(p.getLeftChild(), myWord);
		}
		else {													//myWord>p.key
			if(p.getRightChild()==null)
				return myWord + " : doesn't exist";
			return searchKey(p.getRightChild(), myWord);
		}
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void addKey(BSTNode p,BSTNode fatherOfp , String myWord, File myFile, int indexInFile , boolean isStopWordRoot) {
		
		if (p.getKey() == ""){
			p.setKey(myWord);
		}
		
		if(myWord.compareTo(p.getKey()) == 0){					//myWord==p.key  	sharte khoorooj
			if(p.getLinkList() == null) {
				p.setLinkList(new LinkList());
//				p.setBF(0);
				p.setHeight(1);
				counter++;
			}
			else{
				isRepeated = true;
			}
			LinkListNode lln = new LinkListNode();
			lln.setKey(myFile.getName());
			lln.setIndex(indexInFile);
			lln.setRightNode(p.getLinkList().getRoot());
			p.getLinkList().setRoot(lln);
		}
		
		else if(myWord.compareTo(p.getKey()) < 0){				//myWord<p.key
			if(p.getLeftChild()==null)
				p.setLeftChild(new BSTNode(myWord));
			addKey(p.getLeftChild() , p , myWord, myFile , indexInFile , isStopWordRoot);
			if(!isRepeated){
				p.setHeight(Math.max( p.getLeftHeight() , p.getRightHeight()) + 1 );
				p.setBF( p.getLeftHeight() - p.getRightHeight() );
				balance(p, fatherOfp, isStopWordRoot);
			}
		}
		else {													//myWord>p.key
			if(p.getRightChild() == null)
				p.setRightChild(new BSTNode(myWord));
			addKey(p.getRightChild(), p , myWord, myFile , indexInFile , isStopWordRoot);
			if(!isRepeated){
				p.setHeight(Math.max(p.getLeftHeight(), p.getRightHeight()) + 1 );
				p.setBF( p.getLeftHeight() - p.getRightHeight() );
				balance(p, fatherOfp, isStopWordRoot);
			}
		}

	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean findKey(BSTNode p, String myWord) {
		if (p.getKey() == "")
			return false;
		
		if(myWord.compareTo(p.getKey()) == 0){					//myWord==p.key  	sharte khoorooj
			return true;
		}
		
		else if(myWord.compareTo(p.getKey()) < 0){				//myWord<p.key
			if(p.getLeftChild()==null)
				return false;
			return findKey(p.getLeftChild(), myWord);
		}
		else {													//myWord>p.key
			if(p.getRightChild()==null)
				return false;
			return findKey(p.getRightChild(), myWord);
		}
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void deleteNode(BSTNode p , BSTNode fatherOfp , boolean isRightChild ) {
		StackTreeNode stack = new StackTreeNode();
		
                if(p.getRightChild() == null && p.getLeftChild() == null){
                    if(isRightChild)
                    	fatherOfp.setRightChild(null);
                    else
                    	fatherOfp.setLeftChild(null);
                }
                
                else if(p.getRightChild() == null){
                    if(isRightChild)
                    	fatherOfp.setRightChild(p.getLeftChild());
                    else
                    	fatherOfp.setLeftChild(p.getLeftChild());
                }
                
                else if(p.getLeftChild() == null){
                    if(isRightChild)
                    	fatherOfp.setRightChild(p.getRightChild());
                    else
                    	fatherOfp.setLeftChild(p.getRightChild());
                }
                
                else {
                    BSTNode fatherOfMinInRight = p;
                    BSTNode minInRight = p.getRightChild();
                    stack.push(p);
                    stack.push(minInRight);
                    while (minInRight.getLeftChild() != null){
                    	stack.push(fatherOfMinInRight);
                        fatherOfMinInRight = minInRight;
                        minInRight = minInRight.getLeftChild();
                    }
                    stack.pop();
                    p.setKey(minInRight.getKey());	
                    p.getLinkList().setRoot(minInRight.getLinkList().getRoot());
                    if(p == fatherOfMinInRight)
                        fatherOfMinInRight.setRightChild(minInRight.getRightChild());
                    else
                        fatherOfMinInRight.setLeftChild(minInRight.getRightChild());
                }
                
                p.setHeight(Math.max( p.getLeftHeight() , p.getRightHeight()) + 1 );
                p.setBF( p.getLeftHeight() - p.getRightHeight() );
                
                
                while(!stack.isEmpty()){
                	BSTNode q = (BSTNode) stack.pop();
                	BSTNode fatherOfq;
                	if(stack.isEmpty())
                		fatherOfq = fatherOfp;
                	else
                		fatherOfq = (BSTNode) stack.give();
                	
                	q.setHeight(Math.max( q.getLeftHeight() , q.getRightHeight()) + 1 );
    				q.setBF( q.getLeftHeight() - q.getRightHeight() );
    				if(fatherOfq.getKey().equals("")){
    					balance(q, null, false);
    				}
    				else
    					balance(q, fatherOfq, false);
                }
    }
	

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void balance( BSTNode p , BSTNode fatherOfp , boolean isStopWordRoot ){
		if(fatherOfp!=null && fatherOfp.getKey().equals(""))
			fatherOfp = null;
		
		
		if(p.getBF() == 2){					//p.getBF() can't be -2 
			if(p.getLeftChild().getLeftHeight() > p.getLeftChild().getRightHeight()){
				
				
				BSTNode pLeft = p.getLeftChild();								//LL Case
			     p.setLeftChild(pLeft.getRightChild());
			     pLeft.setRightChild(p);
			 
			     
			     if(fatherOfp != null ){	
			    	 if(fatherOfp.getLeftChild() == p)
			    		 fatherOfp.setLeftChild(pLeft);					//maybe right
			    	 else
			    		 fatherOfp.setRightChild(pLeft);
			     }
			     else{
			    	 if(isStopWordRoot)
			    		 StopWordRoot = pLeft;
			    	 else 
			    		 root = pLeft;
			     }
			    	 
			     p.setHeight(Math.max ( p.getLeftHeight() , p.getRightHeight() ) + 1);
			     p.setBF( p.getLeftHeight() - p.getRightHeight() );
			     pLeft.setHeight(Math.max (pLeft.getLeftHeight() , pLeft.getRightHeight() ) + 1);
			     pLeft.setBF( pLeft.getLeftHeight() - pLeft.getRightHeight() );
			}
			
			
			
			
			
			
			else{								// .. == .. doesn't happen 
				BSTNode pLeft = p.getLeftChild();							//LR Case
			     BSTNode rightOfpLeft = pLeft.getRightChild();
			     p.setLeftChild(rightOfpLeft.getRightChild());
			     pLeft.setRightChild(rightOfpLeft.getLeftChild());
			     rightOfpLeft.setLeftChild(pLeft);
			     rightOfpLeft.setRightChild(p);
			     if(fatherOfp != null ){	
			    	 if(fatherOfp.getLeftChild() == p)
			    		 fatherOfp.setLeftChild(rightOfpLeft);					//maybe right
			    	 else
			    		 fatherOfp.setRightChild(rightOfpLeft);
			     }
			     else{
			    	 if(isStopWordRoot)
			    		 StopWordRoot = rightOfpLeft;
			    	 else 
			    		 root = rightOfpLeft;
			     }
			     
			     p.setHeight( Math.max ( p.getLeftHeight() , p.getRightHeight() ) + 1 );
			     p.setBF( p.getLeftHeight() - p.getRightHeight() );
			     pLeft.setHeight( Math.max ( pLeft.getLeftHeight() , pLeft.getRightHeight() ) + 1 );
			     pLeft.setBF( pLeft.getLeftHeight() - pLeft.getRightHeight() );
			     rightOfpLeft.setHeight( Math.max ( rightOfpLeft.getLeftHeight() , rightOfpLeft.getRightHeight() ) + 1 );
			     rightOfpLeft.setBF( rightOfpLeft.getLeftHeight() - rightOfpLeft.getRightHeight() );
			}
		}
		
		
		
		
		
		
		
		
		else if(p.getBF() == -2){					//p.getBF() can't be +2 
			if(p.getRightChild().getRightHeight() > p.getRightChild().getLeftHeight()){
				
				
				 BSTNode pRight = p.getRightChild();					//RR Case
			     p.setRightChild(pRight.getLeftChild());
			     pRight.setLeftChild(p);
			     if(fatherOfp != null ){
			    	 if(fatherOfp.getRightChild() == p)
			    		 fatherOfp.setRightChild(pRight);							//maybe left
			    	 else
			    		 fatherOfp.setLeftChild(pRight);
			     }
			     else{
			    	 if(isStopWordRoot)
			    		 StopWordRoot = pRight;
			    	 else 
			    		 root = pRight;
			     }
			    	 
			     p.setHeight(Math.max ( p.getLeftHeight() , p.getRightHeight()  ) + 1);
			     p.setBF( p.getLeftHeight() - p.getRightHeight() );
			     pRight.setHeight(Math.max (pRight.getLeftHeight() , pRight.getRightHeight() ) + 1);
			     pRight.setBF( pRight.getLeftHeight() - pRight.getRightHeight() );
			}
			
			
			
			
			
			else{								// .. == .. doesn't happen 
				 BSTNode pRight = p.getRightChild();						//RL Case
			     BSTNode leftOfpRight = pRight.getLeftChild();
			     p.setRightChild(leftOfpRight.getLeftChild());
			     pRight.setLeftChild(leftOfpRight.getRightChild());
			     leftOfpRight.setRightChild(pRight);
			     leftOfpRight.setLeftChild(p);
			     if(fatherOfp != null ){	
			    	 if(fatherOfp.getLeftChild() == p)
			    		 fatherOfp.setLeftChild(leftOfpRight);					//maybe right
			    	 else
			    		 fatherOfp.setRightChild(leftOfpRight);
			     }
			     else{
			    	 if(isStopWordRoot)
			    		 StopWordRoot = leftOfpRight;
			    	 else 
			    		 root = leftOfpRight;
			     }
			     
			     p.setHeight( Math.max ( p.getLeftHeight() , p.getRightHeight() ) + 1 );
			     p.setBF( p.getLeftHeight() - p.getRightHeight() );
			     pRight.setHeight( Math.max ( pRight.getLeftHeight() , pRight.getRightHeight() ) + 1 );
			     pRight.setBF( pRight.getLeftHeight() - pRight.getRightHeight() );
			     leftOfpRight.setHeight( Math.max ( leftOfpRight.getLeftHeight() , leftOfpRight.getRightHeight() ) + 1 );
			     leftOfpRight.setBF( leftOfpRight.getLeftHeight() - leftOfpRight.getRightHeight() );
			}
		}
		
		
		
		
		
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//	private void Copy (StackTreeNode S, StackTreeNode T) {
//		if (!S.isEmpty()) {
//			BSTNode e = (BSTNode) S.pop();
//			 Copy (S, T); 
//			 T.push(e); 
//			 S.push(e);
//		}
//	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void deleteFromLinkListAndIfRootNullDeleteNode (BSTNode p , BSTNode fatherOfp , boolean isRightChild, File myFile ) {
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
					deleteNode(p , fatherOfp , isRightChild );
					needToCheckAVL = true;
				}
				break;
			}
			preTemp = temp;
			temp = temp.getRightNode();
		}
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}