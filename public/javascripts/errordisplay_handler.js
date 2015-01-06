/* Project Arignee ** autor: David Rieger ** 2014 */

/* This JS handles the displaying of
 *error messages on the GUI
 */

/* --------------------- */
/* Error Display Handler */
/* --------------------- */

var error_container = $("#error_container")
var error_message = $("#error_container > #error_message")

function displayError(msg) {
    error_message.html(msg)
    error_container.css("display", "block")
}

function hideAllErrors() {
    error_container.css("display", "none")
}
