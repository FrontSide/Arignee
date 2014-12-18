package collectors.request;

public interface HTTPConnector<T> {

    /**
      * Sends an HTTP Request to
      * @param URL : url to request data from
      * @returns relevant segments of HTTP Resonse from Requested url
      *         in form of an Object from Class T
      */
    public T request(final String URL);

}
