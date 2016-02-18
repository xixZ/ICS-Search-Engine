package indexing;

import javafx.util.Pair;
import java.util.ArrayList;


public class Posting {
    int docFreq;     //number of occurrence in the whole corpus
    ArrayList<Pair<Integer, ArrayList<Integer>>> posting;   //<DocID, positions>
    Posting(){
        docFreq = 0;
        posting = new ArrayList<>();
    }
}
