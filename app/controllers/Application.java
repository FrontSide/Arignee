package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render());
    }
    
    /******
     ** This method only is here for test purposes 
     ** yes I know this doesn't belong here
     ** Later, instead of directly rendering the keyword pupularity
     ** the results are just written in a DB
     *******/
    public static Result kwpot(String kp) {
        //return ok(index.render(GoogleTrendsController.getKeywordTimePopularity(kp)));
        new GoogleTrendsController().getKeywordTimePopularity(kp);
        return index();
    }

}
