package ticketing;

/**
 * This interface must be implemented by all the classes that
 * contribute at processing the request of a client that got a ticket
 * for its request
 */

public interface TicketProcessor {

    /**
     * Sets the number of the Ticket this object is working on
     * @param number ticket-number
     */
    void setTicketNumber(long number);

    /**
     * Initiates to set the status of the ticket this object is working on
     * to a new status
     * @param status new status for ticket
     */
    void updateTicketStatus(TicketStatus status);

}
