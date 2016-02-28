package indexing;


import java.io.*;
import java.util.*;


public class Main {
	static int MAX_FILE_NUM=0;
	static int MAX_PAGE_IN_MEMERY=50000;
	
	public static int processFiles(IndexBuilder iB ) throws FileNotFoundException, IOException{
		int docID = 0;
		int wordPos = 0;
		Map <String,Posting> temp_table = new TreeMap<>();
		Map <Integer,String[]> docID_URL_Title = new HashMap<>();
		
		//===================================READ FILE
		Integer fileNum = 0;
		while(fileNum <= MAX_FILE_NUM){
			System.out.println("build index for file " + fileNum.toString());
			try (BufferedReader br = new BufferedReader(new FileReader("./file/myfile" + fileNum.toString() + ".txt"))) {
				String URL = null;
				for (String line; (line = br.readLine()) != null; ) {	
					if (line.equals("##------------------URL-------------------------##")) {
						if (docID%MAX_PAGE_IN_MEMERY == 0 && docID!=0) {
							iB.buildIndexForaChunk(temp_table);
							temp_table.clear();
						}
						docID++;
						wordPos = 0;
			        	if((line = br.readLine()) != null) URL=line;
					} else if (line.equals("##-----------------TITLE------------------------##")) {
						if((line = br.readLine()) != null) {
							docID_URL_Title.put(docID, new String[] {URL,line});}
					} else if (line.equals("##------------------TEXT------------------------##")) {
		
					} else {
						for (String i : iB.tokenizeFile(line)) {
							if (i.length() > 1) {
								wordPos++;
								if (!temp_table.containsKey(i)) {//new word for big table
									Posting post = new Posting();
									post.wordFreq = 1;
									Map<Integer, ScoreNPosition> docAndPosition = new HashMap<>();
									ArrayList<Integer> newList = new ArrayList<>();
									newList.add(wordPos);
									docAndPosition.put(docID, new ScoreNPosition(0,newList));
									post.posting = docAndPosition;
									temp_table.put(i, post);
								} else {//existing word for big table
									temp_table.get(i).wordFreq++;
									if (temp_table.get(i).posting.containsKey(docID)) {//the word has the page record
										temp_table.get(i).posting.get(docID).postionInDoc.add(wordPos);
									} else {//the word does not have the page record
										ArrayList<Integer> newList = new ArrayList<>();
										newList.add(wordPos);
										temp_table.get(i).posting.put(docID, new ScoreNPosition(0,newList));
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
		iB.storeURL_Map(docID_URL_Title, '$');
		temp_table.clear();
		docID_URL_Title.clear();
		
		iB.buildScore(docID);
		return docID;
	}
	
    public static void main(String[] args) throws IOException{
       	IndexBuilder iB = new IndexBuilder();
        int totalPages = processFiles(iB);
        System.out.println("total Unique Words: "+iB.uniqueWordsCounter());
        System.out.println("total Pages: "+ totalPages);
    	IndexBuilder.printIndextTable('a');
		//iB.printURL();
		
    }

}
