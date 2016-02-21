package indexing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


public class IndexBuilder {
    HashMap<String, Posting> revertedIndex = new HashMap<>();
    
    IndexBuilder(){//INIT
    	for(char i = '0'; i <= '9';i++) {
    		//File varTmpDir = new File("./file/tables/table_"+i+".txt");
    		//if(!varTmpDir.exists())
    		storeMap(revertedIndex,i);
    	}
    	for(char i = 'a'; i <= 'z';i++) {
    		//File varTmpDir = new File("./file/tables/table_"+i+".txt");
    		//if(!varTmpDir.exists())
    		storeMap(revertedIndex,i);
    	}
    }
    
    public void buildIndexForaChunk(Map <String,Posting> tempTable) throws IOException{
    	char lastChar = '~';
    	for(String i : tempTable.keySet() ){
    		if(lastChar!=i.charAt(0)){
    			if(lastChar!='~'){
    				storeMap(revertedIndex,lastChar);
    			}
    			revertedIndex.clear();
    			revertedIndex = readMap(i.charAt(0));
    			lastChar=i.charAt(0);
    		}
    		
			if(!revertedIndex.containsKey(i)){//new word for big table
				revertedIndex.put(i, tempTable.get(i));
			}else{//old word for big table
				revertedIndex.get(i).wordFreq+=tempTable.get(i).wordFreq;
				for(int docID : tempTable.get(i).posting.keySet()){
					revertedIndex.get(i).posting.put(docID, tempTable.get(i).posting.get(docID));
				}
			}
		}
    	storeMap(revertedIndex,lastChar);
		revertedIndex.clear();
    }
    
    public ArrayList<String> tokenizeFile(String fileText){// need some work to delete stop words
    	String[] tokensArray =(fileText.toLowerCase()).split("[^a-zA-Z0-9]");
    	ArrayList<String> tokens = new ArrayList<>(Arrays.asList(tokensArray));
		return tokens;
	}
    
    public void storeMap(HashMap<String, Posting> map, char prefix){
	    //write to file 
	    try{
	    	FileOutputStream fos=new FileOutputStream("./file/tables/table_"+prefix+".txt");
	        ObjectOutputStream oos=new ObjectOutputStream(fos);
	        oos.writeObject(map);
	        oos.flush(); 
	        oos.close();
	        fos.close();
	    }catch(Exception e){}
	}
	public HashMap<String, Posting> readMap(char prefix) throws IOException{
		HashMap<String,Posting> mapInFile = new HashMap<String,Posting>();
   		try{
            FileInputStream fis=new FileInputStream("./file/tables/table_"+prefix+".txt");
            ObjectInputStream ois=new ObjectInputStream(fis);
            mapInFile.putAll((HashMap<String,Posting>)ois.readObject());
            ois.close();
            fis.close();
        }catch(Exception e){}
   		return mapInFile;
	}
}
