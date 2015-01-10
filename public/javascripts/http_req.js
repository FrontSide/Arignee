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
var TICKET_STATUS_BASE_URL = "/ticket/"
var EVAL_URL_TITLE_CONTAINER = $("#eval_url_title")
var ticketnumber
var urlToEvaluate

/**
* Checks if the response from the server contains the ticket
* or the actual requested data
* Initiates to render Data if available
* Necessary for evaluation requests
* @param {String} RESPONSE response from http request
*/
function ticketInspector(RESPONSE) {

    var json = $.parseJSON(RESPONSE)

    //Check if valid ticket is in response
    if (json.TICKET != null) {

        //If this is the first response with the ticket
        //set global ticketnumber for client
        if (ticketnumber <= 0) {
            ticketnumber = json.TICKET
            console.log("TICKET RECEIVED :: " + ticketnumber)
        }

        //Update progressbar according to ticket status
        //Check is error status
        if (json.STATUS == "INVALID_URL") {
            abortPseudoProgress()
            hideProgressbar()
            displayError("<b>" + urlToEvaluate + "</b> is not a valid URL")
            return
        }

        /* Update Progressbar Label and sends a ticket request to the
         * Arignee server after 1 sec
         * ticketInspector is again callback function */
        setProgressbarLabel(json.STATUS)

        setTimeout(function(){
            send(TICKET_STATUS_BASE_URL + ticketnumber, ticketInspector)
        }, 1000)
        return
    }

    //Trigger html_eval_render if ticket has finished and is no more in response
    //i.e. evaluation results are available
    EVAL_URL_TITLE_CONTAINER.html("Evaluation Results for <b>" + urlToEvaluate + "</b>")
    abortPseudoProgress()
    setProgressbarSuccess()
    websiteHtmlEvaluationRender(json)

}

/**
 * Resets all involved GUI elements and initiates an HTTP request for
 * HTML Content Evaluation
 * @param {String} URL url of webpage to evaluate
 */
function websiteHtmlEvaluationRequest(URL){

    //Reset GUI elements
    resetEvalRender()
    resetProgressBar()
    hideAllErrors()

    urlToEvaluate = URL
    EVAL_URL_TITLE_CONTAINER.html("Requesting evaluation for <b>" + urlToEvaluate + "</b>")

    console.log("html_content_request TRIGGERED WITH URL :: " + urlToEvaluate)

    //Assemble URL for HTTP request to Arignee Server
    var URL = HTML_REQ_BASE_URL + urlToEvaluate

    //Reset ticketnumber
    ticketnumber = -1

    //Render ProgressBar
    setProgressbarRequesting()
    updateProgressbar(0)
    showProgressbar()
    setProgressbarLabel("Requesting evaluation of <b>" + URL + "</b>")
    runPseudoProgress(5000, 50)

    /* trigger http request -- send ticketInspector as callback function
     * for received data */
    send(URL, ticketInspector)

}

/**
 * Requests the History for the EvaluationResults for a given URL
 * either for a given category (EvaluatorKey accd. to
 * WebsiteHtmlEvaluatorKey enum) or all of the results for this url if no key
 * given
 * @param {String} URL          Url for which the evaluation-result-history is
 *                              to load
 * @param {String} EVALUATORKEY Category of which the evaluation-result-history
 *                              for the given URL is to load
 */
function websiteHtmlEvaluationHistoryRequest(URL, EVALUATORKEY) {

    

}

/* ---------------------- */
/* Trends KeyWord Fetcher */
/* ---------------------- */

var TRENDS_TIME_BASE_URL = "/keyphrase/time/"

 /* Sends HTTP Response to Application which triggers HTTP response to Google */
function trendsKeywordRequest(keyphrase){

    console.log("trends_kw_request TRIGGERED WITH KEYPHRASE :: " + keyphrase)

    var URL = TRENDS_TIME_BASE_URL + keyphrase
    send(URL)
}

/* ------------------------- */

/**
 * Sends http request and calls the given callback function
 * @param  {String} URL url to request
 * @param  {Function} CALLBACK function to call when data from http-request
 *                             received
 */
function send(URL, CALLBACK) {
    console.log("sending HTTP-Requesting :: " + URL)

    //Send request to server
    $.ajax({ url: URL, type: "GET" }).done(function(data) {
        console.log("setting response :: " + data)
        CALLBACK(data)
    })

}
