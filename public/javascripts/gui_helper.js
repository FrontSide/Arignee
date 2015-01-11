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
     containerWrapper.append(TO_APPEND)
 }

 function setContainerWrapperContent(CONTENT) {
     containerWrapper.html(CONTENT)
 }


 /* returns a Bootstrap colour type (success, danger, warning...)
 * or a propritary one (defined in css)
 */
 function getColourType(RATING) {

     switch(RATING) {
         case "EXCELLENT" :  return "success";
         case "GOOD" :       return "primary";
         case "OK" :         return "info";
         case "TENUOUS" :    return "warning";
         case "POOR" :       return "danger"
         default :           return "default"
     }

 }
