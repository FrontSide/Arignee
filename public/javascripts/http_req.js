/* Project Arignee ** autor: David Rieger ** 2014 */

/* This JS triggers HTTP requests in the Play Application 
 * to fetch data from webservices and websites
 * See WebsiteHtmlCollector/Controller
 * See GoogleTrendsCollector/Controller
 */

/* -------------------- */
/* HTML Content Fetcher */
/* -------------------- */

var HTML_REQ_BASE_URL = "/eval/?url="
 
 /* Sends HTTP Response to Application which triggers HTTP response to Google */
function html_content_request(entered_url){
    
    console.log("html_content_request TRIGGERED WITH URL :: " + entered_url)
    
    var URL = HTML_REQ_BASE_URL + entered_url
    send(URL)    
}

/* ---------------------- */
/* Trends KeyWord Fetcher */
/* ---------------------- */

var TRENDS_TIME_BASE_URL = "/keyphrase/time/"
 
 /* Sends HTTP Response to Application which triggers HTTP response to Google */
function trends_kw_request(keyphrase){
    
    console.log("trends_kw_request TRIGGERED WITH KEYPHRASE :: " + keyphrase)
    
    var URL = TRENDS_TIME_BASE_URL + keyphrase
    send(URL)
}

/* ------------------------- */

function send(URL) {
    $.ajax({ url: URL, type: "GET" }).done(function() {
        alert( "success" )
    })
}

