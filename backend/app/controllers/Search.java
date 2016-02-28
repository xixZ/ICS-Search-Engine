package controllers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.Collections;
import java.util.PriorityQueue;
import controllers.indexing.Posting;
import controllers.indexing.ScoreNPosition;

import play.*;
import play.mvc.*;

import views.html.*;

public class Search extends Controller{
    Integer numOfResults = 20;

    class ScoredDoc implements Comparable<ScoredDoc>{
        Integer docID;
        Double score;
        ScoredDoc(Integer docID, Double score){
            this.docID = docID;
            this.score = score;
        }
        @Override
        public int compareTo(ScoredDoc other) {
            if(score - other.score > 0)
                return 1;
            if(score - other.score < 0)
                return -1;
            return 0;
        }
    }
    /**
     * @param query
     * @return A treeMap which maps score to docID
     */
    private PriorityQueue<ScoredDoc> searchHelper(String query) throws IOException, ClassNotFoundException{
        String[] keyWords = (query.toLowerCase()).split("[^a-zA-Z0-9]");
        if(keyWords.length == 0)
            return null;
        HashMap<Integer, Double> docIdToScore = new HashMap<>();    //docID -> score
        for(String keyWord: keyWords) {
            HashMap<String, Posting> invertedIndex = readMap(keyWord.charAt(0));
            Posting postingObj = invertedIndex.get(keyWord);
            if (postingObj == null)
                continue;
            Map<Integer, ScoreNPosition> pos = postingObj.posting;
            Set<Integer> docIDs = pos.keySet();
            for(Integer docID: docIDs){
                Double score = docIdToScore.get(docID);
                if (score == null){
                    docIdToScore.put(docID, (pos.get(docID)).score);
                } else{
                   docIdToScore.put(docID, score + (pos.get(docID)).score);
                }
            }
        }
        Set<Integer> docIDs = docIdToScore.keySet();
        PriorityQueue<ScoredDoc> result = new PriorityQueue<>();
        for(Integer docID: docIDs) {
            ScoredDoc sd = new ScoredDoc(docID, docIdToScore.get(docID));
            if(result.size() == numOfResults) {
                if (sd.score > result.peek().score) {
                    result.poll();
                    result.add(sd);
                }
            } else{
                result.add(sd);
            }
        }
        return result;
    }
    public Result search(String query) throws IOException, ClassNotFoundException{
        System.out.println("################ received query " + query);
        PriorityQueue<ScoredDoc> docs = searchHelper(query);
    	Map<Integer,String[]> URL_Title_table = readURL_Map();

        String[] title;
        String[] url;
        String[] content;

        if (docs != null) {
            numOfResults = docs.size();
            title = new String[numOfResults];
            url = new String[numOfResults];
            content = new String[numOfResults];
            int i = numOfResults - 1;
            Integer docID;
            while(docs.size() > 0){
                ScoredDoc sd = docs.poll();
                docID = sd.docID;
                title[i] = URL_Title_table.get(docID)[1];
                url[i] = URL_Title_table.get(docID)[0];
                content[i] = "docID: " + docID.toString() + " score: " + sd.score;
                i --;
            }
        } else {
            numOfResults = 0;
            title = new String[numOfResults];
            url = new String[numOfResults];
            content = new String[numOfResults];
        }
        return ok(index.render(title, url, content, numOfResults));
    }

	public HashMap<String, Posting> readMap(char prefix) throws IOException, ClassNotFoundException{
		HashMap<String,Posting> mapInFile = new HashMap<>();
        String filePath = "./file/tables/table_" + prefix + ".txt";
        FileInputStream fis=new FileInputStream(filePath);
        ObjectInputStream ois=new ObjectInputStream(fis);
        mapInFile.putAll((HashMap<String,Posting>)ois.readObject());
        ois.close();
        fis.close();
   		return mapInFile;
	}

	public Map<Integer, String[]> readURL_Map() throws IOException{
		Map<Integer, String[]> mapInFile = new HashMap<>();
   		try{
            FileInputStream fis=new FileInputStream("./file/tables/table_$.txt");
            ObjectInputStream ois=new ObjectInputStream(fis);
            mapInFile.putAll((Map<Integer, String[]>)ois.readObject());
            ois.close();
            fis.close();
        }catch(Exception e){
            System.out.println("in readURL_Map exception");
        }
   		return mapInFile;
	}
}
