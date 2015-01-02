
package collectors;

    /**
     * This class supertypes the GoogleTrends Collector(Strategies)
     * it overloads the get method so that a keyphrase can be accepted from
     * the controllers and implements the urlBuilder which creates the url
     * out of the keyphrase and given url parameters
     */

import java.util.Map;
import java.util.List;
import collectors.enums.CollectorKey;
import models.collection.CollectorValue;

public class GoogleTrendsCollector<T>   extends AbstractCollector<T> 
                                        implements CollectorStrategyContext {

    /**
    * Stores the concrete Strategy class/object
    */
    private GoogleTrendsCollectorStrategy strategy;

    public GoogleTrendsCollector() {}

    public GoogleTrendsCollector(GoogleTrendsCollectorStrategy strategy) {
        this.strategy = strategy;
    }

    private void passData() {
        ((Collector)this.strategy).raw(this.raw());
        ((Collector)this.strategy).url(this.url());
    }


    /* Base URL and paths For Google Trends to fetch components
     * (add "q=" for keyphrase)
     */
    //private static final String BASEURL =
    //"http://weather.yahooapis.com/forecastrss?p=80020&u=f";
    protected static final String BASEURL =
                                "http://www.google.com/trends/fetchComponent";

    //The CID to fetch the timeseries of a keyword (add with "cid=")
    protected static final String TIMESERIES_CID = "TIMESERIES_GRAPH_0";

    //The Number to Export the result as JSON (add with "export=")
    protected static final String JSON_EXPORT_NUMBER = "3";

    /**
     * Stores the keyphrase that is to be looked up with Google Trends
     */
    private String keyphrase;

    /**
     * Overloads the get Method from the AbstractCollector
     * with a String parameter for the Search/Key-phrase for GoogleTrends
     * @param  kephrase GoogleTrends Key/Search Phrase
     * @return          A Map with all Collected and extracted data
     */
    public Map<? extends CollectorKey, CollectorValue> get(String kephrase)
                                                    throws RuntimeException {
        this.keyphrase(kephrase);
        this.buildUrl();
        return this.get();
    }

    public Map<? extends CollectorKey, CollectorValue> executeExtract() {
        return this.strategy.extractFromRaw();
    }

    /**
     * Setter for the GoogleTrends Keyphrase
     * @param   keyphrase
     * @return  this
     */
    public GoogleTrendsCollector keyphrase(String keyphrase) {
        this.keyphrase = keyphrase;
        return this;
    }

    @Override
    public void buildUrl() {
        this.url("http://www.google.com/trends/fetchComponent"+
                        "?q=asdf,qwerty&cid=TIMESERIES_GRAPH_0&export=3");
        /*this.url = BASEURL + "?q=" + this.keyphrase +
                  "&cid=" + TIMESERIES_CID + "&export=" + JSON_EXPORT_NUMBER;*/
    }



}
