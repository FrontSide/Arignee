
package controllers;

/**
 * This class parses and renders the responses from the GoogleTrendsCollector
 * which fetches data from GoogleTrends
 */
 
import collectors.*;
import play.mvc.*;

public class GoogleTrendsController extends Controller {

    public String getKeywordTimePopularity(String keyword) {
        return new GoogleTrendsCollectorFactory().create().fetchTimeseries(keyword);
    }

}
