package controllers;

import java.io.Serializable;
import java.util.ArrayList;

public class ScoreNPosition implements Serializable{
	double score;
	ArrayList<Integer> postionInDoc;
	
	ScoreNPosition(double score, ArrayList<Integer>postionInDoc){
		this.score=score;
		this.postionInDoc=postionInDoc;
	}
}
