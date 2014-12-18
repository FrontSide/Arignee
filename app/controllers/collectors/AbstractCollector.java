package collectors;

/**
  * The Abstraction for Collectors
  * The Generic Type T is the Type that is expected to be delivered
  * by the HTTPConnector and used by the Collector for the extraction process
  */

public abstract class AbstractCollector<T> implements Collector {

    protected final Map<Key, List<String>> COLLECTED;    
    protected String url;
    
    protected final HTTPConnector connector 
        = new HttpConnectorFactory(this).create();
    
    protected T raw;
    
    public Map<Key, List<String>> get() throws RuntimeException {
        throw new IllegalStateException("No Collector Specified");
    }

    protected void fetch() {
    
        try {
            T response = (T) this.connector.request(this.url);
        } catch (ClassCastException e) {
            Logger.error("Type Missmatch between Object delivered from HTTPConnector and Collector");
            return;
        }
        
        if (response == null) 
            throw new RuntimeException("Requesting HTTP Response for URL " + 
                                                this.url + " failed");
        this.raw = response;
    }
    
    protected void extract();
   
    public String url();
    
    public String raw();

}
