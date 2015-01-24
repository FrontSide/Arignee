package collectors.request;

/**
 *
 */

import java.net.URL;
import java.net.MalformedURLException;

public interface HTTPConnectorStrategy<T> {

    /**
    * Sends an HTTP Request to
    * @param URL : url to request data from
    * @returns relevant segments of HTTP Resonse from Requested url
    *         in form of an Object from Class T
    */
    public T request(final URL URL) throws MalformedURLException;

}
