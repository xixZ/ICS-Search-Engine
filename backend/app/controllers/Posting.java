package controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;


public class Posting implements Serializable{
	int wordFreq;     //number of occurrence in the whole corpus
    Map<Integer, ScoreNPosition> posting;   //<DocID, positions>
    Posting(){
    }
}
