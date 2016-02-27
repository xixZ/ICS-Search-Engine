package controllers;

import play.*;
import play.mvc.*;

import views.html.*;
import controllers.indexing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
public class BuildIndex extends Controller {
    public Result index() throws FileNotFoundException, IOException{
        IndexBuilder iB = new IndexBuilder();
        int totalPages = FileProcessor.processFiles(iB);
        System.out.println("total Unique Words: "+iB.uniqueWordsCounter());
        System.out.println("total Pages: "+ totalPages);
        return ok("Index Build Success");
    }
}