package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Search extends Controller {

    public Result search(String query) {
        return ok("received query: " + query);
    }

}
