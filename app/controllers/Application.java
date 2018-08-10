package controllers;


import play.mvc.Controller;
import play.mvc.Result;

import static play.libs.Json.toJson;

public class Application extends Controller {

    public Result index() {
        return ok(toJson("message: Welcome to the beginnings of the Dead by Daylight Build Maker"));
    }

}
