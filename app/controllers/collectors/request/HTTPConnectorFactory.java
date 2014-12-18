package request;

/**
  * This is a Factory for HTTPConnectors
  * The type of HTTPConnector that is created depends on 
  * the class of Collector that's instanciating the Factory
  *
  * This class has to be called from a Collector i.e. with a Colleector object
  * handed through the Constructor
  *
  * Singleton
  */

public class HTTPConnectorFactory {

    private HTTPConnectorFactory() {}
    
    private static HTTPConnectorFactory instance;
    public HTTPConnectorFactory createFactory() {
        if (this.instance == null) this.instance = new HTTPConnectorFactory();
        return this.instance;
    }
    
    /*
     * Create the needed HTTPConnector and returns it
     */
    public HTTPConnector create(Collector c) {
    
        if (c instanceof WebsiteHtmlCollector) {
            return new JsoupHTTPConnector();
        }
        
        return null;
                
    }

}
