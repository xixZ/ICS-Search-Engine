package indexing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


public class IndexBuilder {
	
    HashMap<String, Posting> revertedIndex = new HashMap<>();
    
    public void buildIndex(String[] tokens, Integer docID,Integer wordCount){
    	int cnt = 1;
		for(String i : tokens){
			if(!revertedIndex.containsKey(i)){//new word for big table
				Posting post = new Posting();
				post.wordFreq=1;
				Map <Integer, ArrayList<Integer>> docAndPosition = new HashMap<>();
				ArrayList<Integer> newList = new ArrayList<>(500);
				newList.add(wordCount+cnt);
				docAndPosition.put(docID, newList);
				post.posting=docAndPosition;
				revertedIndex.put(i, post);
			}else{//old word for big table
				revertedIndex.get(i).wordFreq++;
				if(revertedIndex.get(i).posting.containsKey(docID)){//the word has the page record
					revertedIndex.get(i).posting.get(docID).add(wordCount+cnt);
				}else{//the word does not have the page record
					ArrayList<Integer> newList = new ArrayList<>(500);
    				newList.add(wordCount+cnt);
    				revertedIndex.get(i).posting.put(docID, newList);
				}
			}
			cnt++;
		}
    }
    
    
    public String[] tokenizeFile(String fileText){
    	String[]tokens=(fileText.toLowerCase()).split("[^a-zA-Z0-9]");		
		return tokens;
	}
    

}
