package indexing;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import jdk.internal.util.xml.impl.Pair;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;


public class Main {
	
	
    public static void main(String[] args) throws FileNotFoundException, IOException{
    	
    	HashMap <String,Posting> indexTable = new HashMap<>();
        
        IndexBuilder iB = new IndexBuilder();
        
    	int pageCount = 0;
		int wordCount = 0;
		
		//===================================READ FILE
		try (BufferedReader br = new BufferedReader(new FileReader("./file/myfile1.txt"))) {
		    for(String line; (line = br.readLine()) != null; ) {
		        if(line.equals("##--------------------------------------------------------##")){
		        	//System.out.println("Page #"+cnt+": "+wordsCnt);
		        	pageCount++;
		        	wordCount=0;
		        }else{
		    		List <String> tokens= iB.tokenizeFile(line);
		    		int cnt = 1;
		    		for(String i : tokens){
		    			if(!indexTable.containsKey(i)){//not contain
		    				Posting post = new Posting();
		    				post.wordFreq=1;
		    				Map <Integer, ArrayList<Integer>> docAndPosition = new HashMap<>();
		    				docAndPosition.put(pageCount, new ArrayList<Integer>(wordCount+cnt));
		    				post.posting=docAndPosition;
		    				indexTable.put(i, post);
		    			}else{
		    				indexTable.get(i).wordFreq++;
		    				
		    				if(indexTable.get(i).posting.containsKey(pageCount)){
		    					indexTable.get(i).posting.get(pageCount).add(wordCount+cnt);
		    				}else{
		    					indexTable.get(i).posting.put(pageCount, new ArrayList<Integer>(wordCount+cnt));
		    				}
		    			}
		    			cnt++;
		    		}
		    		wordCount+=tokens.size();
		        }
		    }
		}
		
		Scanner user_input = new Scanner(System.in);
		
		
		//for(String i:indexTable.keySet()){
			//System.out.println(i);
		//}
		
		//user input:
		String query1;
		System.out.println("Enter your query please, dude: ");
		query1 = user_input.next();
		user_input.close();
		
		
		//fetch result  HUGE CHANGE NEED TO BE HAPPEN HERE TOOOOOO SLOWWWW:
		Set<Integer> resultPageNumber = indexTable.get(query1).posting.keySet();
		List<String> returnURL = new ArrayList<>();
		
		System.out.println("Bro, here is your results: ");
		for(int pageNumber : resultPageNumber){
			try (BufferedReader br2 = new BufferedReader(new FileReader("./file/urls1.txt"))) {    
				int URLcnt=1;
				for(String line; (line = br2.readLine()) != null; ) {
			        if(URLcnt==pageNumber){
			        	returnURL.add(line);
			        	System.out.println(line);
			        }
			        URLcnt++;
				}	
			}
		}
		
    }
}
