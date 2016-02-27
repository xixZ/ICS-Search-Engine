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
import java.util.Set;
import controllers.indexing.Posting;
import controllers.indexing.ScoreNPosition;

import play.*;
import play.mvc.*;

import views.html.*;

public class Search extends Controller{
    Integer numOfResults = 10;
    public Result search(String query) throws IOException, ClassNotFoundException{
    	HashMap<String, Posting> temp_table = readMap(query.charAt(0));
    	Map<Integer,String[]> URL_Title_table = readURL_Map();

    	Posting result = temp_table.get(query);
    	
    	Set<Integer> docIDs = result.posting.keySet();
    	numOfResults = Math.min(10, docIDs.size());
    	
    	String[] title = new String[numOfResults];
        String[] url = new String[numOfResults];
        String[] content = new String[numOfResults];

        int i = 0;
     	for(Integer docID: docIDs){
            title[i] = Integer.toString(i)+": " + URL_Title_table.get(docID)[1];
            url[i] = URL_Title_table.get(docID)[0];
            content[i] = "I am the " + Integer.toString(i) + "th result"+" for "+query;
            i ++;
        }
        return ok(index.render(title, url, content, numOfResults));
    }
    
    public ArrayList<String> tokenizeFile(String fileText){// need some work to delete stop words
    	String[] tokensArray =(fileText.toLowerCase()).split("[^a-zA-Z0-9]");
    	ArrayList<String> tokens = new ArrayList<>(Arrays.asList(tokensArray));
		return tokens;
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
