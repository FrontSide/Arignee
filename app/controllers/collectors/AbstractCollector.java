package collectors;

/**
  * The Abstraction for Collectors
  * The Generic Type T is the Type that is expected to be delivered
  * by the HTTPConnector and used by the Collector for the extraction process
  */
  
import java.util.Map;
import java.util.List;
import collectors.enums.Key;
import collectors.request.*;
import play.Logger;

public abstract class AbstractCollector<T> implements Collector {

    private Map<Key, List<String>> collected;
        
    private final HTTPConnector CONNECTOR 
                        = HTTPConnectorFactory.getInstance().create(this);
    
    private T raw;
    
    private String url;
    
    public Map<? extends Key, List<String>> get() throws RuntimeException {
        if (this.url == null)   {
            Logger.error("No URL!"); 
            Logger.warn("Try to invoke with a parameter");
            Logger.error("Killing collector...");
            return null;
        }
        this.fetch(this.url);
        return this.extract(this.raw);
    }

    /**
     * Uses an before instanziated HTTPConnector and triggers its HTTP-Request
     * Result is saved in 
     */
    private void fetch(String url) {
    
        T response = null;
    
        try {            
            Logger.debug("Trigger HTTP Connector Request");
            response = (T) this.CONNECTOR.request(url);
        } catch (ClassCastException e) {
            Logger.error("Type Missmatch between Object delivered " + 
                            "from HTTPConnector and Collector");
            return;
        }
        
        if (response == null) 
            throw new RuntimeException("Requesting HTTP Response for URL " + 
                                                url + " failed");
                                        
        Logger.debug("Response from HTTP Connector received!");
        this.raw = response;
    }
    
    /**
     * Extracts the useful data that is later used by an Evaluator
     * from the fetched (raw) data returned from the HTTPConnector
     */
    protected abstract Map<? extends Key, List<String>> extract(T raw);
   
    public Collector url(String url) {
        this.url = url;
        return this;
    }
   
    public String url() {
        return this.url;
    }
    
    public void buildUrl() {
        throw new UnsupportedOperationException("buildUrl() not implemented");
    }
    
    public String raw() {
        return this.raw.toString();
    }

}
