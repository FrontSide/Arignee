/* Project Arignee ** autor: David Rieger ** 2014 */

/* GUI Helper */

/*
 * Helps managing GUI Elements
 */

 var containerWrapper = $("#eval_res_wrapper")

 function showContainerWrapper() {
     containerWrapper.css("display", "block")
 }

 function hideContainerWrapper() {
     containerWrapper.css("display", "none")
 }

 /* Delete all the content from the container */
 function resetEvalRender() {
     containerWrapper.html("")
 }

 function appendContainerWrapperContent(TO_APPEND) {
     containerWrapper.html(containerWrapper.html() + TO_APPEND)
 }

 function setContainerWrapperContent(CONTENT) {
     containerWrapper.html(CONTENT)
 }
