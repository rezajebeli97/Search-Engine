import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class TST implements Tree {
	private TSTNode StopWordRoot = new TSTNode();
	private TSTNode root = new TSTNode();
	public ArrayList<String> myFiles = new ArrayList<String>();
	private ArrayList<StringAndName> myWholeFiles = new ArrayList<StringAndName>();
	private int counter=0;
	private boolean needToDelNode = false;
	private boolean needToCheckBlnce = false;
	private boolean needToCheckBalance = false;
	private String s,sCheck;
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public String build(File StopWordFile , File myFile) {
			
			Scanner scannerStopWords = null;			
				try {
					scannerStopWords = new Scanner(StopWordFile);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			
			while (scannerStopWords.hasNextLine()) {
				String myStopWord = scannerStopWords.nextLine();
				String[] stopWords = myStopWord.split("[^a-zA-Z]+");
				for (String string : stopWords) {
					string = string.toLowerCase();
					needToCheckBalance = false;
					addKey(StopWordRoot , null , string , 0 , StopWordFile , 0 , true);
				}
			}
	
		
		
			counter = 0;
			
		long start2 =System.currentTimeMillis();
						
		for (File file : myFile.listFiles()) {
			myFiles.add(file.getName()); 						//array of my files
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
					if( !string.equals("") && !findKey(StopWordRoot, string, 0) ){		//check if the word doesn't be a stopWord if(findKey(StopWordRoot , myWord))
						needToCheckBalance = false;
						addKey(root , null , string, 0 , file , indexInFile , false);
					}
				}										
			}
			myWholeFiles.add(myFileString);
		}
		
		long finish2 = System.currentTimeMillis();
		return "TST : " + (finish2-start2) + "ms\nNumber Of Words : " + counter + "\nHeight of balance tree : " + root.getHeight() + "\nNumber Of listed files : " + myFiles.size();	
}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public String add(File AddFile) {
		for (String string : myFiles) {
			if(string.equals(AddFile.getName())){			
				return "err: already exists, you may want to update.";								//return , if(!flag) isn't necessary
			}	
		}
		if(!AddFile.exists())
			return "err: document not found.";
		
		
		myFiles.add(AddFile.getName());
		StringAndName stringAndname = new StringAndName();
		stringAndname.namePart = AddFile.getName();
		stringAndname.filePart = "";
		
		Scanner scannerAdd = null ;
		
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
				if( !string.equals("") && !findKey(StopWordRoot, string, 0)){		//check if the word doesn't be a stopWord if(findKey(StopWordRoot , myWord))
					needToCheckBalance = false;
					addKey(root , null , string, 0 , AddFile , indexInFile , false);
				}
			}
		}
		myWholeFiles.add(stringAndname);	
		return AddFile.getName()+" successfully added.";
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

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

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
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
					if (!string.equals("") && !findKey(StopWordRoot, string, 0)){ 			// check if the word doesn't be a stopWord if(findKey(StopWordRoot , myWord))
						//TSTNode fatherOfRoot = new TSTNode();
						//fatherOfRoot.setKey(' ');
				        //fatherOfRoot.setRightChild(root);
				       // boolean isRightChild = true;
				        needToDelNode = false;
				        needToCheckBlnce = false;
				        deleteKey(root , null , string , 0 ,/*isRightChild ,*/ DeleteFile);
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
	
	private void deleteKey(TSTNode p , TSTNode fatherOfp , String myWord , int index, File myFile){
		char c = myWord.charAt(index);
		
		if (c < p.getKey()){
			deleteKey(p.getLeftChild() , p , myWord, index,myFile);
			if(needToDelNode){
				deleteNode(p, p.getLeftChild(), 3);
				needToDelNode = false;
			}
			if(needToCheckBlnce){
				p.setHeight(Math.max( p.getLeftHeight() , p.getRightHeight()) + 1 );
				p.setBF( p.getLeftHeight() - p.getRightHeight() );
				balance(p, fatherOfp, false);
			}
		} 
		else if (c > p.getKey()){
			 deleteKey(p.getRightChild() , p , myWord, index,myFile);
			 if(needToDelNode){
				 deleteNode(p, p.getRightChild(), 2);
				 needToDelNode = false;
			 }
			 if(needToCheckBlnce){
					p.setHeight(Math.max( p.getLeftHeight() , p.getRightHeight()) + 1 );
					p.setBF( p.getLeftHeight() - p.getRightHeight() );
					balance(p, fatherOfp, false);
				}
		} 
		
		else if (c == p.getKey() && index == myWord.toCharArray().length - 1 ) {
			if(p.isEnd() && p.getLinkList().getRoot()!=null){								//momkene ye kalame 2 bar too ye file ii bashe va faghat ham too hamoon file bashe pas momkene bare aval ke delete mikonim linklist kollan null she va baraye bare 2vom kari baraye anjam dadan nist(asan too if narim: null pointer exeption)
				deleteFromLinkListAndIfRootNullDeleteNode(p, /*fatherOfp, isRightChild ,*/ myFile);
			}
		}
		
		else if (c == p.getKey()){
			deleteKey(p.getMiddleChild() , p , myWord, index + 1 , myFile);
			if(needToDelNode){
				deleteNode(p , p.getMiddleChild() , 1);
			}
			if(needToCheckBlnce){
				p.setHeight(Math.max( p.getLeftHeight() , p.getRightHeight()) + 1 );
				p.setBF( p.getLeftHeight() - p.getRightHeight() );
				balance(p, fatherOfp, false);
			}
		}
		
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void deleteNode(TSTNode fatherOfp , TSTNode p , int MidOrRightOrLeft) {
		StackTreeNode stack = new StackTreeNode();
		
		if(p.getRightChild()==null && p.getLeftChild()==null){
			if(MidOrRightOrLeft == 1)
				fatherOfp.setMiddleChild(null);
			if(MidOrRightOrLeft == 2)
				fatherOfp.setRightChild(null);
			if(MidOrRightOrLeft == 3)
				fatherOfp.setLeftChild(null);
			
			if(fatherOfp.isEnd() == true)
				needToDelNode = false;
		}
		else if(p.getRightChild()!=null && p.getLeftChild()==null){
			if(MidOrRightOrLeft == 1)
				fatherOfp.setMiddleChild(p.getRightChild());
			if(MidOrRightOrLeft == 2)
				fatherOfp.setRightChild(p.getRightChild());
			if(MidOrRightOrLeft == 3)
				fatherOfp.setLeftChild(p.getRightChild());
			needToDelNode = false;
		}
		else if(p.getRightChild()==null && p.getLeftChild()!=null){
			if(MidOrRightOrLeft == 1)
				fatherOfp.setMiddleChild(p.getLeftChild());
			if(MidOrRightOrLeft == 2)
				fatherOfp.setRightChild(p.getLeftChild());
			if(MidOrRightOrLeft == 3)
				fatherOfp.setLeftChild(p.getLeftChild());
			needToDelNode = false;
		}
		else {
			TSTNode fatherOfMinInRight = p;
			TSTNode minInRight = p.getRightChild();
			stack.push(p);
			stack.push(minInRight);
			while (minInRight.getLeftChild() != null){
				stack.push(fatherOfMinInRight);
				fatherOfMinInRight = minInRight;
				minInRight = minInRight.getLeftChild();
			}
			stack.pop();
			p.setKey(minInRight.getKey());
			p.setLinkList(minInRight.getLinkList());
			p.setMiddleChild(minInRight.getMiddleChild());
			if(p == fatherOfMinInRight)
				fatherOfMinInRight.setRightChild(minInRight.getRightChild());
			else
				fatherOfMinInRight.setLeftChild(minInRight.getRightChild());
        
			needToDelNode = false;
		}
		
		
		
		p.setHeight(Math.max( p.getLeftHeight() , p.getRightHeight()) + 1 );
        p.setBF( p.getLeftHeight() - p.getRightHeight() );
        
        
        while(!stack.isEmpty()){
        	TSTNode q = (TSTNode) stack.pop();
        	TSTNode fatherOfq;
        	if(stack.isEmpty())
        		fatherOfq = fatherOfp;
        	else
        		fatherOfq = (TSTNode) stack.give();
        	
        	q.setHeight(Math.max( q.getLeftHeight() , q.getRightHeight()) + 1 );
			q.setBF( q.getLeftHeight() - q.getRightHeight() );
			if(fatherOfq.getKey() == ' ' ){
				balance(q, null, false);
			}
			else
				balance(q, fatherOfq, false);
        }
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
				if(findKey(StopWordRoot, string, 0)){
					continue;
				}
				s="";
				String searchResult = searchKey(root, string, 0);
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
		return searchKey(root, myWord, 0);
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
		peymayeshTst(root , "" );
		return counter;
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void peymayeshTst(TSTNode p , String myWord ) {
		if(p==null)
			return;
		
		if(p.getLeftChild() != null ){
			if(myWord.length()==0)
				peymayeshTst( p.getLeftChild() , myWord + p.getLeftChild().getKey() );
			else
				peymayeshTst( p.getLeftChild() , myWord.substring(0, myWord.length() - 1 ) + p.getLeftChild().getKey() );
		}
			
		if(p.getMiddleChild() != null )
			if( myWord.length()==0 ){
				peymayeshTst(p.getMiddleChild(), myWord + p.getKey() + p.getMiddleChild().getKey());
				myWord = p.getKey()+"";
			}
			else
				peymayeshTst(p.getMiddleChild(), myWord + p.getMiddleChild().getKey());
		
		if(p.getRightChild() != null ){
			if( myWord.length()==0 )
				peymayeshTst( p.getRightChild() , myWord + p.getRightChild().getKey());
			else
				peymayeshTst( p.getRightChild() , myWord.substring(0, myWord.length() - 1 ) + p.getRightChild().getKey());
		}
		
		if(p.isEnd()){
			counter++;
			MyFrame.textArea.setText(MyFrame.textArea.getText() + myWord + " -> ");
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

	}
	

	
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private String searchKey(TSTNode p , String myWord , int index){
		char c = myWord.charAt(index);
		
		if(p == null)
			return myWord + " : doesn't exist";
		
		if (c < p.getKey()){
			return searchKey(p.getLeftChild(), myWord, index);
		} 
		else if (c > p.getKey()){
			return searchKey(p.getRightChild(), myWord, index);
		} 
		
		else if (c == p.getKey() && index == myWord.toCharArray().length - 1 ) {
			if(p.isEnd()){
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
			else
				return myWord + " : doesn't exist";
		}
		
		else if (c == p.getKey()){
			return searchKey(p.getMiddleChild(), myWord, index + 1);
		}
						
		else{						//alaki : just for ignore eclipse error
			return myWord + " : doesn't exist";
		}
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void addKey(TSTNode p , TSTNode fatherOfp, String myWord, int index, File myFile, int indexInFile ,  boolean isStopWordRoot) {
		char c = myWord.charAt(index);
		if(p == null)
			p = new TSTNode();
		
		if (p.getKey() == 0)
			p.setKey(c);

		if (c < p.getKey()){
			if(p.getLeftChild() == null){
				p.setLeftChild(new TSTNode());
				p.getLeftChild().setHeight(1);
				//p.getLeftChild().setBF(0);
			}
			addKey(p.getLeftChild() , p , myWord, index ,myFile , indexInFile , isStopWordRoot);
			if(needToCheckBalance){
				p.setHeight(Math.max( p.getLeftHeight() , p.getRightHeight()) + 1 );
				p.setBF( p.getLeftHeight() - p.getRightHeight() );
				balance(p, fatherOfp, isStopWordRoot);
			}
		} 
		else if (c > p.getKey()){
			if(p.getRightChild()==null){
				p.setRightChild(new TSTNode());
				p.getRightChild().setHeight(1);
				//p.getRightChild().setBF(0);
			}
			addKey(p.getRightChild() , p , myWord, index , myFile , indexInFile , isStopWordRoot);
			if(needToCheckBalance){
				p.setHeight(Math.max( p.getLeftHeight() , p.getRightHeight()) + 1 );
				p.setBF( p.getLeftHeight() - p.getRightHeight() );
				balance(p, fatherOfp, isStopWordRoot);
			}
		} 
		
		else if (c == p.getKey() && index == myWord.toCharArray().length - 1) {
			p.setEnd(true);
				
			if(p.getLinkList() == null) {
				p.setLinkList(new LinkList());
				counter++;
				needToCheckBalance = true;
			}
			LinkListNode lln = new LinkListNode();
			lln.setKey(myFile.getName());
			lln.setIndex(indexInFile);
			lln.setRightNode(p.getLinkList().getRoot());
			p.getLinkList().setRoot(lln);
		}
		
		else if (c == p.getKey()){
			if(p.getMiddleChild()==null){
				p.setMiddleChild(new TSTNode());
				p.getMiddleChild().setHeight(1);
				//p.getMiddleChild().setBF(0);
				p.getMiddleChild().setKey(myWord.charAt(++index));
				index--;
			}
			addKey(p.getMiddleChild() , p , myWord, index + 1 , myFile , indexInFile , isStopWordRoot);
			if(needToCheckBalance){
				p.setHeight(Math.max( p.getLeftHeight() , p.getRightHeight()) + 1 );
				p.setBF( p.getLeftHeight() - p.getRightHeight() );
				balance(p, fatherOfp, isStopWordRoot);
			}
		}

	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private boolean findKey(TSTNode p , String myWord , int index){
		char c = myWord.charAt(index);
		
		if(p == null)
			return false;
		
		if (c < p.getKey()){
			return findKey(p.getLeftChild(), myWord, index);
		} 
		else if (c > p.getKey()){
			return findKey(p.getRightChild(), myWord, index);
		} 
		
		else if (c == p.getKey() && index == myWord.toCharArray().length - 1 ) {
			if(p.isEnd())
				return true;
			else
				return false;
		}
		
		else if (c == p.getKey()){
			return findKey(p.getMiddleChild(), myWord, index + 1);
		}
						
		else{						//alaki : just for ignore eclipse error
			return false;
		}
		
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void deleteFromLinkListAndIfRootNullDeleteNode (TSTNode p /*, TSTNode fatherOfp , boolean isRightChild*/, File myFile ) {
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
					if(p.getMiddleChild()==null){
						needToDelNode=true;
						needToCheckBlnce = true;
					}
					//deleteNode(p , fatherOfp , isRightChild );
					//needToCheckAVL = true;
				}
				break;
			}
			preTemp = temp;
			temp = temp.getRightNode();
		}
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void balance(TSTNode p, TSTNode fatherOfp, boolean isStopWordRoot) {
		
		
		if(p.getBF() == 2){					//p.getBF() can't be -2 
			if(p.getLeftChild().getLeftHeight() > p.getLeftChild().getRightHeight()){
				
				
				TSTNode pLeft = p.getLeftChild();								//LL Case
			     p.setLeftChild(pLeft.getRightChild());
			     pLeft.setRightChild(p);
			 
			     
			     if(fatherOfp != null ){	
			    	 if(fatherOfp.getLeftChild() == p)
			    		 fatherOfp.setLeftChild(pLeft);					//maybe right
			    	 else if (fatherOfp.getRightChild() == p)
			    		 fatherOfp.setRightChild(pLeft);
			    	 else
			    		 fatherOfp.setMiddleChild(pLeft);
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
				TSTNode pLeft = p.getLeftChild();							//LR Case
				TSTNode rightOfpLeft = pLeft.getRightChild();
			     p.setLeftChild(rightOfpLeft.getRightChild());
			     pLeft.setRightChild(rightOfpLeft.getLeftChild());
			     rightOfpLeft.setLeftChild(pLeft);
			     rightOfpLeft.setRightChild(p);
			     if(fatherOfp != null ){	
			    	 if(fatherOfp.getLeftChild() == p)
			    		 fatherOfp.setLeftChild(rightOfpLeft);					//maybe right
			    	 else if(fatherOfp.getRightChild() == p)
			    		 fatherOfp.setRightChild(rightOfpLeft);
			    	 else
			    		 fatherOfp.setMiddleChild(rightOfpLeft);
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
				
				
				TSTNode pRight = p.getRightChild();					//RR Case
			     p.setRightChild(pRight.getLeftChild());
			     pRight.setLeftChild(p);
			     if(fatherOfp != null ){
			    	 if(fatherOfp.getRightChild() == p)
			    		 fatherOfp.setRightChild(pRight);							//maybe left
			    	 else if(fatherOfp.getLeftChild() == p)
			    		 fatherOfp.setLeftChild(pRight);
			    	 else 
			    		 fatherOfp.setMiddleChild(pRight);
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
				TSTNode pRight = p.getRightChild();						//RL Case
				TSTNode leftOfpRight = pRight.getLeftChild();
			     p.setRightChild(leftOfpRight.getLeftChild());
			     pRight.setLeftChild(leftOfpRight.getRightChild());
			     leftOfpRight.setRightChild(pRight);
			     leftOfpRight.setLeftChild(p);
			     if(fatherOfp != null ){	
			    	 if(fatherOfp.getLeftChild() == p)
			    		 fatherOfp.setLeftChild(leftOfpRight);					//maybe right
			    	 else if(fatherOfp.getRightChild() == p)
			    		 fatherOfp.setRightChild(leftOfpRight);
			    	 else
			    		 fatherOfp.setMiddleChild(leftOfpRight);
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
}