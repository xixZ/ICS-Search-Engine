package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public Result index() {
        return redirect("/assets/WebUI/index.html");
    }

}
