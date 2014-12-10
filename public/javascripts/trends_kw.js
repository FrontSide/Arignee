/* Project Arignee ** autor: David Rieger ** 2014 */

/* Trends KeyWord Fetcher */

/* This JS sends an HTTP request to google trends and 
 * requests json data for the given keyphrase entered by the user 
 */

/* Variable Definition */
//Base URL For Google Trends to fetch components (add "q=" for keyphrase)
var TRENDS_REQUEST_BASE_URL = "https://www.google.com/trends/fetchComponent"

//The CID to fetch the timeseries of a keyword (add with "cid=")
var TRENDS_REQUEST_TIMESERIES_CID = "TIMESERIES_GRAPH_0"

//The Number to Export the result as JSON (add with "export=")
var TRENDS_REQUEST_JSON_EXPORT_NUMBER = "3"

 
 /* Sends request with Keyphrase Included */
function trends_kw_request(keyphrase){

    var REQUEST_URL = 
            TRENDS_REQUEST_BASE_URL + 
            "?q=" + keyphrase +
            "&cid=" + TRENDS_REQUEST_TIMESERIES_CID + 
            "&export=" + TRENDS_REQUEST_JSON_EXPORT_NUMBER
            
    console.log("trends_kw-request-URL::" + REQUEST_URL)
    
    $.ajax({ dataType: 'text', url: REQUEST_URL })
    
    /*$.ajax({
        url: REQUEST_URL,
        type: "GET",
        dataType: "jsonp",
        jsonp : false,
        jsonpCallback: 'jsonCallback',
        cache: 'true'
    }).done(function() {
      alert( "success" );
    }); */
    
    /*
    var jqxhr = $.get(REQUEST_URL, function() {
        alert( "success" );
    })
    .done(function() {
        alert( "second success" );
    })
    .fail(function() {
        alert( "error" );
    })
    .always(function() {
        alert( "finished" );
    }); */
}
  
