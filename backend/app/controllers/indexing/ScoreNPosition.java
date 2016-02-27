package controllers.indexing;

import java.io.Serializable;
import java.util.ArrayList;

public class ScoreNPosition implements Serializable{
    public double score;
    public ArrayList<Integer> postionInDoc;

    public ScoreNPosition(double score, ArrayList<Integer>postionInDoc){
        this.score=score;
        this.postionInDoc=postionInDoc;
    }
}
