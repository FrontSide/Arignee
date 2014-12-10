
package controllers;

/*
 * This class parses and renders the responses from the GoogleTrendsCollector
 * which fetches data from GoogleTrends
 */

public class GoogleTrendsController extends Controller {

    public String getKeywordTimePopularity(String keyword) {
        return new GoogleTrendsCollector().fetchTimeseries(keyword);
    }

}
