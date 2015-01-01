package collectors.request;

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

import collectors.Collector;
import collectors.WebsiteHtmlJsoupCollector;

public class HTTPConnectorFactory {

    private HTTPConnectorFactory() {}

    private static final HTTPConnectorFactory INSTANCE = new HTTPConnectorFactory();
    public static final HTTPConnectorFactory getInstance() {
        return HTTPConnectorFactory.INSTANCE;
    }

    /*
     * Create the needed HTTPConnector and returns it
     */
    public HTTPConnector create() {

        return new HTTPConnector(new JsoupHTTPConnector());

    }

}
