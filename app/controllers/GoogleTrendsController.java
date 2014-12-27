
package controllers;

/**
 * This class parses and renders the responses from the GoogleTrendsCollector
 * which fetches data from GoogleTrends
 */
 
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import collectors.GoogleTrendsCollectorFactory;
import collectors.GoogleTrendsCollector;
import collectors.CollectorValue;
import collectors.enums.CollectorKey;
import java.util.List;
import play.mvc.*;

public class GoogleTrendsController extends Controller {

    private final GoogleTrendsCollectorFactory COLLECTORFACTORY = 
                                GoogleTrendsCollectorFactory.getInstance();

    public Map<String, List<String>> getKeywordTimePopularity(String keyword) {
        
        GoogleTrendsCollector collector = this.COLLECTORFACTORY.create();
        Map<? extends CollectorKey, CollectorValue> data = collector.get(keyword);
        
         //TODO: pass data to evaluator
        return new HashMap<String, List<String>>();
        
    }

}
