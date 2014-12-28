/* Project Arignee ** autor: David Rieger ** 2014 */

/* Progress Bar Handler */

/* This JS handles the actions and the rendering of the progress bar
 * used while axecuting http requests and waiting for the response
 */
 
var progress_bar_container = $("#progress_bar_container")
var progress_bar = $("#progress_bar_container > #progress_bar")
var progress_bar_label = $("#progress_bar_container > #progress_bar > #progress_bar_label")
 
function showProgressbar() {
    progress_bar_container.css("display", "block")
}
 
function hideProgressbar() {
    progress_bar_container.css("display", "none")
}

function setProgressbarLabel(TEXT) {
    progress_bar_label.html(TEXT)
}

function setProgressbarSuccess() {
    progress_bar.removeClass("progress-bar-warning")
    progress_bar.addClass("progress-bar-success")
}

function setProgressbarRequesting() {
    progress_bar.removeClass("progress-bar-success")
    progress_bar.addClass("progress-bar-warning")
}

function currentProgress() {
    return parseInt($('.progress-bar').attr('aria-valuenow'))
}

function updateProgressbar(NEW_VALUE) {
    $('.progress-bar').css('width', NEW_VALUE+'%').attr('aria-valuenow', NEW_VALUE)
} 

function incrementProgressbar(INCREMENT) {
    updateProgressbar(currentProgress() + INCREMENT)
}

/* Increases the Progress bar continiously within [TIME] 
 * milliseconds up until the MAX_VALUE 
 * Is used to show the user that something's happening without actual
 * dependency on any data-progess
 */
ABORT_PSUDO_PROGESS = false;
function runPseudoProgress(TIME, MAX_VALUE) {
    
    if(currentProgress() >= MAX_VALUE || ABORT_PSUDO_PROGESS) return
    incrementProgressbar(1)        
    timestep = TIME/MAX_VALUE            
    setTimeout(function(){runPseudoProgress(TIME, MAX_VALUE)}, timestep)

}
function abortPseudoProgress(){
    ABORT_PSUDO_PROGESS=true
}



