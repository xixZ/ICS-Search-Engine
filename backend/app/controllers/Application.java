package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public Result index() {
        return redirect("http://localhost:9000/assets/WebUI/index.html");
    }

}
