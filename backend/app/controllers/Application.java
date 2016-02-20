package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public Result index() {
        return redirect("http://52.36.193.45/assets/WebUI/index.html");
    }

}
