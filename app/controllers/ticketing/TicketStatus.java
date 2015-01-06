package ticketing;

/**
 * Enum for the Status of a Ticket that is requested by the client for
 * GUI updates and set by the server-side
 */

public enum TicketStatus {

    /* Pre-Evaluation Statuses */
    QUEUING, /* The Request -this Ticket is assigned to- has not yet been processed */
    STARTING, /* Assigned once the controller has started processing the ticket */
    REQUESTING, /* The HttpRequestor is requesting an HTTP-Response */
    EXTRACTING, /* The collector is extracting data from the raw HTTP-Response */

    /* Evaluation Statuses */
    PAGE_TITLE_EVAL,
    LINK_AMOUNT_EVAL,
    INTERN_BACKLINK_RATIO_EVAL,
    EXTERN_BACKLINK_RATIO_EVAL,

    /* Post-Evaluation Statuses */
    EVALUATION_FINISHED, /* Assigned as soon as the evaluator has passed a result to the controller */
    RESPONSE_AVAILABLE, /* The ServerSide has finished and the data can now be sent to the client */

    /* Error Statuses */
    INVALID_URL, /* The entered URL is invalid */
    ERROR, /* An error occured and the request cannot be further processes */

    /* No Ticket statses */
    TICKET_DELETED, /* When a ticket no longer exists (but once has) */
    TICKET_NOT_FOUND /* When a ticket does not and has never existed */

}
