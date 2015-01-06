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

 /* Sends HTTP Response to Application which triggers HTTP response to Google */
function html_content_request(entered_url){

    resetEvalRender()
    resetProgressBar()
    hideAllErrors()

    urlToEvaluate = entered_url
    EVAL_URL_TITLE_CONTAINER.html("Requesting evaluation for <b>" + urlToEvaluate + "</b>")

    console.log("html_content_request TRIGGERED WITH URL :: " + urlToEvaluate)

    var URL = HTML_REQ_BASE_URL + urlToEvaluate

    //Reset ticketnumber
    ticketnumber = -1

    //Render ProgressBar
    setProgressbarRequesting()
    updateProgressbar(0)
    showProgressbar()
    setProgressbarLabel("Requesting evaluation of <b>" + URL + "</b>")
    runPseudoProgress(5000, 50)

    //Fetch response - expect ticket status
    send(URL)

}

function html_eval_processor(response) {
    /* Request status of ticket until evaluation response is here */
    var json = $.parseJSON(response)

    //Check if valid ticket is in response
    if (json.TICKET > -1) {

        //If this is the first response with the ticket
        //set global ticketnumber for client
        if (ticketnumber == -1) {
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
        setProgressbarLabel(json.STATUS)
        console.log("TICKET STATUS :: " + json.STATUS)

        setTimeout(function(){send(TICKET_STATUS_BASE_URL + ticketnumber)}, 1000)

    //Trigger html_eval_rendering if ticket has finished and is no more in response
    //i.e. evaluation results are available
    } else {
        EVAL_URL_TITLE_CONTAINER.html("Evaluation Results for <b>" + urlToEvaluate + "</b>")
        abortPseudoProgress()
        setProgressbarSuccess()
        html_eval_render(json)
    }
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
var progress_bar_container = $("#progress_bar_container")


function send(URL) {
    console.log("sending HTTP-Requesting :: " + URL)

    //Send request to server
    $.ajax({ url: URL, type: "GET" }).done(function(data) {
        console.log("setting response :: " + data)
        html_eval_processor(data);
    })

}
