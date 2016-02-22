package indexing;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import scala.collection.mutable.Set;


public class Main {
	static int MAX_FILE_NUM=0;
	static int MAX_PAGE_IN_MEMERY=50000;
	
	public static int processFiles(IndexBuilder iB ) throws FileNotFoundException, IOException{
		int docID = 0;
		int wordPos = 0;
		Map <String,Posting> temp_table = new TreeMap<>();
		Map <Integer,String> docID_URL = new HashMap<>();
		
		//===================================READ FILE
		Integer fileNum = 0;
		while(fileNum <= MAX_FILE_NUM){
			System.out.println("build index for file " + fileNum.toString());
			try (BufferedReader br = new BufferedReader(new FileReader("./file/myfile" + fileNum.toString() + ".txt"))) {
				for (String line; (line = br.readLine()) != null; ) {
					if (line.equals("##------------------URL-------------------------##")) {
						if (docID%MAX_PAGE_IN_MEMERY == 0 && docID!=0) {
							iB.buildIndexForaChunk(temp_table);
							temp_table.clear();
						}
						docID++;
						wordPos = 0;
			        	if((line = br.readLine()) != null) docID_URL.put(docID, line);
					} else if (line.equals("##-----------------TITLE------------------------##")) {
		
					} else if (line.equals("##------------------TEXT------------------------##")) {
		
					} else {
						for (String i : iB.tokenizeFile(line)) {
							if (i.length() > 1) {
								wordPos++;
								if (!temp_table.containsKey(i)) {//new word for big table
									Posting post = new Posting();
									post.wordFreq = 1;
									Map<Integer, ArrayList<Integer>> docAndPosition = new HashMap<>();
									ArrayList<Integer> newList = new ArrayList<>();
									newList.add(wordPos);
									docAndPosition.put(docID, newList);
									post.posting = docAndPosition;
									temp_table.put(i, post);
								} else {//existing word for big table
									temp_table.get(i).wordFreq++;
									if (temp_table.get(i).posting.containsKey(docID)) {//the word has the page record
										temp_table.get(i).posting.get(docID).add(wordPos);
									} else {//the word does not have the page record
										ArrayList<Integer> newList = new ArrayList<>();
										newList.add(wordPos);
										temp_table.get(i).posting.put(docID, newList);
									}
								}
							}
						}
					}
					//if(docID==3)break;break;
				}
				br.close();
			}
			fileNum ++;
		}
		//add the remaining to table
		iB.buildIndexForaChunk(temp_table);
		iB.storeURL_Map(docID_URL, '$');
		temp_table.clear();
		docID_URL.clear();
		return docID;
	}
	
    public static void main(String[] args) throws FileNotFoundException, IOException{        
        IndexBuilder iB = new IndexBuilder();
        int totalPages = processFiles(iB);
        System.out.println("total Unique Words: "+iB.uniqueWordsCounter());
        System.out.println("total Pages: "+ totalPages);
    	//iB.printURL();
    	//iB.printIndextTable('a');
        /*
		
		Scanner user_input = new Scanner(System.in);
		//user input:
		String query1;
		System.out.println("Enter your query please, dude: ");
		query1 = user_input.next();
		user_input.close();
		
		
		//fetch result  HUGE CHANGE NEED TO BE HAPPEN HERE TOOOOOO SLOWWWW:
		Set resultPageNumber = (Set) iB.readMap(query1.charAt(0)).get(query1).posting.keySet(); 
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
		}*/
		
    }

}
