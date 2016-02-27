package controllers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import controllers.Posting;
import controllers.ScoreNPosition;

import play.*;
import play.mvc.*;

import views.html.*;

public class Search extends Controller {
    Integer numOfResults = 10;
    public Result search(String query) throws IOException{
    	Map<String, Posting> temp_table = readMap(query.charAt(0));
    	Map<Integer,String[]> URL_Title_table = readURL_Map();
    	Posting result = temp_table.get(query);
    	Set<Integer> docIDs = result.posting.keySet();
    	numOfResults = Math.min(10, docIDs.size());
    	
    	
    	String[] title = new String[numOfResults];
        String[] url = new String[numOfResults];
        String[] content = new String[numOfResults];
        
        int i=0;
        for(int docID:docIDs) {
        	if(i>=numOfResults) break;
        	i++;
            title[i] = URL_Title_table.get(docID)[1];
            url[i] = URL_Title_table.get(docID)[0];
            content[i] = "I am the " + Integer.toString(i) + "th result"+" for "+query;
        }
    	
        /*String[] title = new String[numOfResults];
        String[] url = new String[numOfResults];
        String[] content = new String[numOfResults];
        for(int i = 0; i < numOfResults; i ++) {
            title[i] = Integer.toString(i);
            url[i] = "http://google.com";
            content[i] = "I am the " + Integer.toString(i) + "th result"+" for "+query;
        }*/
        return ok(index.render(title, url, content, numOfResults));
    }
    
    public ArrayList<String> tokenizeFile(String fileText){// need some work to delete stop words
    	String[] tokensArray =(fileText.toLowerCase()).split("[^a-zA-Z0-9]");
    	ArrayList<String> tokens = new ArrayList<>(Arrays.asList(tokensArray));
		return tokens;
	}
    
	public HashMap<String, Posting> readMap(char prefix) throws IOException{
		HashMap<String,Posting> mapInFile = new HashMap<>();
   		try{
            FileInputStream fis=new FileInputStream("C:/Users/henry91w/Documents/workspace/SearchEngine/ICS-Search-Engine/file/tables/table_"+prefix+".txt");
            ObjectInputStream ois=new ObjectInputStream(fis);
            mapInFile.putAll((HashMap<String,Posting>)ois.readObject());
            ois.close();
            fis.close();
        }catch(Exception e){}
   		return mapInFile;
	}

	public Map<Integer, String[]> readURL_Map() throws IOException{
		Map<Integer, String[]> mapInFile = new HashMap<>();
   		try{
            FileInputStream fis=new FileInputStream("C:/Users/henry91w/Documents/workspace/SearchEngine/ICS-Search-Engine/file/tables/table_$.txt");
            ObjectInputStream ois=new ObjectInputStream(fis);
            mapInFile.putAll((Map<Integer, String[]>)ois.readObject());
            ois.close();
            fis.close();
        }catch(Exception e){}
   		return mapInFile;
	}

}
