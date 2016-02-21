package indexing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;


public class Posting implements Serializable{
	int wordFreq;     //number of occurrence in the whole corpus
    Map<Integer, ArrayList<Integer>> posting;   //<DocID, positions>
    Posting(){
    }
}
