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
import java.util.TreeMap;

import jdk.internal.util.xml.impl.Pair;

import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;


public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException{        
        IndexBuilder iB = new IndexBuilder();

    	int docID = 0;
		int wordPos = 0;
		
		Map <String,Posting> temp_table = new TreeMap<>();
		//===================================READ FILE
		Integer fileNum = 0;
		while(fileNum < 10){
			System.out.println("build index for file " + fileNum.toString());
			try (BufferedReader br = new BufferedReader(new FileReader("./file/myfile" + fileNum.toString() + ".txt"))) {
				for (String line; (line = br.readLine()) != null; ) {
					if (line.equals("##------------------URL-------------------------##")) {
						if (docID % 10000 == 0) {
							iB.buildIndexForaChunk(temp_table);
							temp_table.clear();
						}
						docID++;
						wordPos = 0;

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
				}
				br.close();
			}
			fileNum ++;
		}
		//add the remaining to table
		iB.buildIndexForaChunk(temp_table);
    	temp_table.clear();
    	
    	
    	//======================================================================================================
    	//======================================��������ĸ���ĸ���Դ���� ����
		
		//iB.storeMap(iB.revertedIndex, '1');
		HashMap<String, Posting> testMap1 = iB.readMap('a');
		

	
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
}
