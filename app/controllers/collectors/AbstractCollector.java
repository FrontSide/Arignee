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
    
    /**
      * Checks if a URL is within a domain i.e. has the same Base-URL
      * @param domain_url : the base-url of the referenced domain
      * @param final URL : the url or path that is checked
      * @returns : true if the passed url is considered an internal url or
      *           merely a path
      */
    public static boolean isInternalUrl(String domainUrl, final String URL) {
        if (domainUrl.equals(URL)) return true;
        if (AbstractCollector.isUrlAppendix(URL)) return true;        
        domainUrl = AbstractCollector.trimToBaseUrl(domainUrl);
        return URL.contains(domainUrl);
    }
    
    /**
     * Checks if a String is merely a Path, Parameter or
     * Resource Identifier (i.e. #) rather than a full URL
     * @param s : String to check
     * @returns : true if String is a url- path, parameter or resource identifier
     */
    public static boolean isUrlAppendix(String s) {
        return "#?/".contains("" + s.charAt(0));
    }
    
    /**
     * Removes paths, protocols and subdomains from a URL to leave just the base
     * and Top-Level Domain (e.g http://dary.info/blog/bac1 --> dary.info)
     * @param url : url to trim
     * @returns : baseUrl (incl. top-level Domain)
     */
    public static String trimToBaseUrl(String url) {        
        url = removeProtocols(url);
        url = url.replace("www.", "");
        
        int shlashIndex = url.indexOf('/');
        if (shlashIndex == -1) return url;
        return url.substring(0, shlashIndex);
    }
    
    /**
      * Checks if two URLs are the same i.e. directing to the same Page
      * @params urla, urlb
      * @returns : true if URLs are the same otherwise false
      */
    public static boolean isSameUrl(String urla, String urlb) {
        if (urla.equals(urlb)) return true;
        urla = AbstractCollector.removeProtocols(urla);
        urlb = AbstractCollector.removeProtocols(urlb);
        
        int hashtagIndex =  urla.indexOf('#');
        if (hashtagIndex != -1) urla = urla.substring(0, hashtagIndex);
        
        hashtagIndex = urlb.indexOf('#');
        if (hashtagIndex != -1) urlb = urlb.substring(0, hashtagIndex);
                
        return (urla.equals(urlb) || urla.equals("/") || urlb.equals("/"));
        
    }
    
    /**
      * Removes the Protocols from a 
      * @param url; and
      * @returns it
      */
    private static String removeProtocols(String url) {
        String[] urlarray = url.split("://");
        if (urlarray.length > 1) url = urlarray[1];
        return url;
    }

}
