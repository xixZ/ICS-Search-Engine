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
import java.util.Map;


public class IndexBuilder {
    HashMap<String, Posting> revertedIndex = new HashMap<>();
    
    IndexBuilder(){//INIT
    	//File urlFile = new File("./file/tables/table_$.txt");
		//if(!urlFile.exists())
    	storeURL_Map(null,'$');//$ table is for urls
    	for(char i = '0'; i <= '9';i++) {
    		//File tableFile = new File("./file/tables/table_"+i+".txt");
    		//if(!tableFile.exists())
    		storeMap(revertedIndex,i);
    	}
    	for(char i = 'a'; i <= 'z';i++) {
    		//File tableFile = new File("./file/tables/table_"+i+".txt");
    		//if(!tableFile.exists())
    		storeMap(revertedIndex,i);
    	}
    }
    
    public void buildIndexForaChunk(Map <String,Posting> tempTable) throws IOException{
    	char lastChar = '~';
    	for(String i : tempTable.keySet() ){
    		if(lastChar!=i.charAt(0)){
    			if(lastChar!='~')
    				storeMap(revertedIndex,lastChar);
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
    	if(lastChar!='~')
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
		HashMap<String,Posting> mapInFile = new HashMap<>();
   		try{
            FileInputStream fis=new FileInputStream("./file/tables/table_"+prefix+".txt");
            ObjectInputStream ois=new ObjectInputStream(fis);
            mapInFile.putAll((HashMap<String,Posting>)ois.readObject());
            ois.close();
            fis.close();
        }catch(Exception e){}
   		return mapInFile;
	}
	public void storeURL_Map(Map<Integer, String> map, char prefix){
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
	public Map<Integer, String> readURL_Map(char prefix) throws IOException{
		Map<Integer, String> mapInFile = new HashMap<>();
   		try{
            FileInputStream fis=new FileInputStream("./file/tables/table_"+prefix+".txt");
            ObjectInputStream ois=new ObjectInputStream(fis);
            mapInFile.putAll((Map<Integer, String>)ois.readObject());
            ois.close();
            fis.close();
        }catch(Exception e){}
   		return mapInFile;
	}

    public void printIndextTable(char prefix) throws IOException{
		HashMap<String, Posting> testMap1 = readMap(prefix);
		for(String i:testMap1.keySet()){
			System.out.print(i+": Fre: "+testMap1.get(i).wordFreq+" ");
			for(int j : testMap1.get(i).posting.keySet()){//doc id
				System.out.print("(docID: "+j+" ");
				for(int k : testMap1.get(i).posting.get(j)){
					System.out.print(" - "+k);
				}
				System.out.print(") ");
			}
			System.out.println();
		}
    }
    public void printURL() throws IOException{
    	Map<Integer, String> urlMap = readURL_Map('$');
		for(int i:urlMap.keySet())
			System.out.println("DocID:  "+i+": "+urlMap.get(i));
    }
    public int uniqueWordsCounter() throws IOException{//INIT
    	int totalUniqueWords = 0;
    	for(char i = '0'; i <= '9';i++) { 
    		totalUniqueWords+=readMap(i).size();
    	}
    	for(char i = 'a'; i <= 'z';i++) {
    		totalUniqueWords+=readMap(i).size();
    	}
    	return totalUniqueWords;
    }
	
}
