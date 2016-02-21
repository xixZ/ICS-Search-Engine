package indexing;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        IndexBuilder iB = new IndexBuilder();

    	int docID = 0;
		int wordCount = 0;
		//===================================READ FILE
		try (BufferedReader br = new BufferedReader(new FileReader("./file/myfile1.txt"))) {
		    for(String line; (line = br.readLine()) != null; ) {
		        if(line.equals("##--------------------------------------------------------##")){
		        	//System.out.println("Page #"+cnt+": "+wordsCnt);
		        	docID++;
		        	wordCount=0;
		        }else{
		    		String[] tokens= iB.tokenizeFile(line);	
		    		iB.buildIndex(tokens, docID, wordCount);
		    		wordCount+=tokens.length;
		        }
		    }
		}
		
		
		
		//Store map and read Map
		storeMap(iB.revertedIndex, 1);
		HashMap<String, Posting> testMap = readMap(1);
		
		//TRY this for printing disk storage map keys
		for(String i:testMap.keySet()){
			System.out.println(i);
		}
		
		//------>
		//TRY this for printing memory storage map keys 
		//for(String i:iB.revertedIndex.keySet()){
		//	System.out.println(i);
		//}
		
		
		
		/*
		Scanner user_input = new Scanner(System.in);
		//user input:
		String query1;
		System.out.println("Enter your query please, dude: ");
		query1 = user_input.next();
		user_input.close();
		
		
		//fetch result  HUGE CHANGE NEED TO BE HAPPEN HERE TOOOOOO SLOWWWW:
		Set<Integer> resultPageNumber = iB.revertedIndex.get(query1).posting.keySet();
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
		*/
    }
    public static void storeMap(HashMap<String, Posting> map, Integer tableNumber){
	    //write to file 
	    try{
	    	FileOutputStream fos=new FileOutputStream("./file/table"+tableNumber.toString()+".txt");
	        ObjectOutputStream oos=new ObjectOutputStream(fos);
	        oos.writeObject(map);
	        oos.close();
	        fos.close();
	    }catch(Exception e){}
	}
	public static HashMap<String, Posting> readMap(Integer tableNumber) throws IOException{
		HashMap<String,Posting> mapInFile = new HashMap<String,Posting>();
   		try{
            FileInputStream fis=new FileInputStream("./file/table"+tableNumber.toString()+".txt");
            ObjectInputStream ois=new ObjectInputStream(fis);
            mapInFile.putAll((HashMap<String,Posting>)ois.readObject());
            ois.close();
            fis.close();
        }catch(Exception e){}
   		return mapInFile;
	}
		
}
