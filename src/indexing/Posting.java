package indexing;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Map;


public class Posting {
    int wordFreq;     //number of occurrence in the whole corpus
    Map<Integer, ArrayList<Integer>> posting;   //<DocID, positions>
  
    Posting(){
    }
}
