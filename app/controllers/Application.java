package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

import java.util.Map;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.mvc.BodyParser;
import play.mvc.BodyParser.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render(null));
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
     **
     ** Returns a JSON with the results that is rendered by JS
     *******/
    @BodyParser.Of(Json.class)
    public static Result htmlfetch(String url) {
        return ok(new JSONObject(new WebsiteHtmlController().evalHtml(url)).toString());
    }
    
}
