package indexing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;


public class Posting implements Serializable{
    public int wordFreq;     //number of occurrence in the whole corpus
    public Map<Integer, ScoreNPosition> posting;   //<DocID, positions>
}


