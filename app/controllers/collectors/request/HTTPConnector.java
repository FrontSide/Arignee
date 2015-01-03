package collectors.request;

/**
 * HTTPConnector Strategy Context
 */

import play.Logger;
import play.Logger.ALogger;

public class HTTPConnector<T> {

    private static final ALogger logger = Logger.of(HTTPConnector.class);

    HTTPConnectorStrategy strategy;

    public HTTPConnector() {}
    public HTTPConnector(HTTPConnectorStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Trigger Request function in concrete HTTP-Requestor Strategy
     * @param  String URL to send request to
     * @return        [description]
     */
    public T executeRequest(final String URL) {
        HTTPConnector.logger.debug("triggering concrete HTTPConnector strategy...");
        return (T) this.strategy.request(URL);
    }

}
