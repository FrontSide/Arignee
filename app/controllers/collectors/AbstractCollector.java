package collectors;

/**
  * The Abstraction for Collectors
  * The Generic Type T is the Type that is expected to be delivered
  * by the HTTPConnector and used by the Collector for the extraction process
  */
  
import java.util.Map;
import java.util.List;
import collectors.enums.CollectorKey;
import collectors.request.*;
import play.Logger;

public abstract class AbstractCollector<T> implements Collector {
        
    private final HTTPConnector CONNECTOR 
                        = HTTPConnectorFactory.getInstance().create(this);
    
    private T raw;
    
    private String url;
    
    /**
      * @returns : collected List 
      */
    public Map<? extends CollectorKey, CollectorValue> get() {
        
        if (this.raw == null) {
            try { 
                this.fetch(); 
            } catch (RuntimeException e) {
                Logger.error("Fetching failed"); 
            }
        }
        return this.extract();
    }

    /**
     * Uses an before instanziated HTTPConnector and triggers its HTTP-Request
     * Result is saved in 
     */
    public Collector fetch() throws RuntimeException {
    
        if (this.url == null) throw new RuntimeException("No URL found!");    
        T response = null;
    
        try {            
            Logger.debug("Trigger HTTP Connector Request");
            response = (T) this.CONNECTOR.request(this.url);
        } catch (ClassCastException e) {
            Logger.error("Type Missmatch between Object delivered " + 
                            "from HTTPConnector and Collector");
            return this;
        }
        
        if (response == null) 
            throw new RuntimeException("Requesting HTTP Response for URL " + 
                                                this.url + " failed");
                                        
        Logger.debug("Response from HTTP Connector received!");
        this.raw = response;
        return this;
    }
    
    /**
     * Extracts the useful data that is later used by an Evaluator
     * from the fetched (raw) data returned from the HTTPConnector
     */
    protected abstract Map<? extends CollectorKey, CollectorValue> extract();
   
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
    
    public T raw() {        
        if (this.raw == null) Logger.error("No raw data found!");
        return this.raw;
    }

}
