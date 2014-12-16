package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

import java.util.Map;

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
    
    /******
     ** This method only is here for test purposes 
     ** yes I know this doesn't belong here
     ** Later, instead of directly rendering the html content
     ** the results are just written in a DB
     *******/
    public static Result htmlfetch(String url) {
        return ok(index.render(new WebsiteHtmlController().evalHtml(url)));
    }
    
}
