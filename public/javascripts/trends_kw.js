/* Project Arignee ** autor: David Rieger ** 2014 */

/* Trends KeyWord Fetcher */

/* This JS triggers an HTTP request to google trends 
 * to fetch data about keyword popularity
 * See GoogleTrendsCollector/Controller
 */

var TRIGGER_BASE_URL = "/keyphrase/time/"
 
 /* Sends HTTP Response to Application which triggers HTTP response to Google */
function trends_kw_request(keyphrase){
    
    console.log("trends_kw_request TRIGGERED WITH KEYPHRASE :: " + keyphrase);
    
    var URL = TRIGGER_BASE_URL + keyphrase;
    
    $.ajax({ url: URL, type: "GET" }).done(function() {
        alert( "success" );
    });
    
}
