package indexing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class IndexBuilder {
    HashMap<String, Posting> revertedIndex = new HashMap<>();
    //public void buildIndex(ArrayList<String> tokens, Integer docID){
    	
    //}
    
    
    public List<String> tokenizeFile(String fileText){
		StringTokenizer st = new StringTokenizer(fileText,"\t\r\n ,,.;:'\""+"!?@#$%^&*_-=+`~/\\"+"()[]{}<>");  
	    List<String> tokensList = new ArrayList<String>();
		while (st.hasMoreTokens()) {  
			tokensList.add(st.nextToken().toLowerCase());
			//System.out.println(st.nextToken()); 
	    }
		return tokensList;
	}
}
