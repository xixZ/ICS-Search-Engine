package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Search extends Controller {
    final Integer numOfResults = 10;
    public Result search(String query) {
        String[] title = new String[numOfResults];
        String[] url = new String[numOfResults];
        String[] content = new String[numOfResults];
        for(int i = 0; i < numOfResults; i ++) {
            title[i] = Integer.toString(i);
            url[i] = "http://google.com";
            content[i] = "I am the " + Integer.toString(i) + "th result";
        }
        return ok(index.render(title, url, content, numOfResults));
    }

}
