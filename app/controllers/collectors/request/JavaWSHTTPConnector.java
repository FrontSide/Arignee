package request;

/*
 * This is an HTTP Connector that sends and receives HTTP Requests/Responses
 * throught the Play JabaWS Library
 */
 
 public JavaWSHTTPConnector implements HTTPConnector<String> {
 
    public String request(final String URL) {
    
        Logger.debug("Request url :: " + URL);
                        
        Promise<String> textResponsePromise =
                WS.url(URL).get().map(new Function<WSResponse, String>() {
                    public String apply(WSResponse response) {
                        String result = response.getBody();
                        return result;
                    }
                }); 

        Logger.debug("done.");
        Logger.debug("receive response...");
        String response = textResponsePromise.get(20000L);
        Logger.debug("response is :: " + response);
        
        return response;
    
    }
 
 }
