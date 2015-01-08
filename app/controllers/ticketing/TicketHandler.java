package ticketing;

/**
* This Controller Handles Tickets for HTTPRequests/Clients.
*
* Example: A user wants to evaluate a Website and enters its URL
* 			it is first routed to this Ticket Generator where it gets a
* 			Number which represents a Ticket.
* 			The evaluation is then asynchronously executed in the background.
* 			The ticket however is linked to the evaluation-Process.
* 			Whenever the client now sends a request with the Ticket-Number
* 			it receives either a status update of the evaluation process
* 			or if the evaluation process has finished for this ticket
* 			the client receives the Results which can then be rendered by JS.
*
* 			Singleton
*/

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import play.mvc.*;
import play.libs.F.*;
import play.Logger;
import play.Logger.ALogger;

import models.ticketing.Ticket;

public class TicketHandler {

    private static final ALogger logger = Logger.of(TicketHandler.class);

    private TicketHandler(){}
    private static TicketHandler instance;
    public static TicketHandler getInstance() {
        if (TicketHandler.instance == null)
        TicketHandler.instance = new TicketHandler();
        return TicketHandler.instance;
    }

    private List<Ticket> tickets = new ArrayList<>();

    public String getNextTicketNumber() {
        return UUID.randomUUID().toString();
    }

    public String getNewTicket() {
        Ticket t = new Ticket();
        this.tickets.add(t);
        return t.getNumber();
    }

    public void updateStatus(String number, TicketStatus status) {
        logger.info("updating status of ticket :: "
                    + number + " :: to :: " + status);
        this.getTicket(number).setStatus(status);
    }

    /**
     * Mark a Ticket as finished and ready to have result obtained
     * @param number number of ticket to mark as finished
     */
    public void markAsFinished(String number) {
        this.getTicket(number).setFinished();
    }

    /**
     * Passes a promise of a Result to a ticket
     * @param  number   number of the ticket to pass result to
     * @param  result   Promise of the result
     */
    public void passResponse(String number, Promise<Result> result) {
        this.getTicket(number).setResponse(result);
    }

    public TicketStatus getStatus(String number) {
        Ticket t = getTicket(number);
        /* TODO: Look for the Ticket Numer in the DB if it's not in the list
         * It might already have been used
         */
        return (t == null) ? TicketStatus.TICKET_NOT_FOUND : t.getStatus();
    }

    public Result getResponse(String number) {
        Ticket t = this.getTicket(number);
        if (!t.isFinished()) throw new TicketNotFinishedException();
        logger.info("trying to obtain stored response from ticket :: " + number);
        Result r = t.getResponse().get(30000L);
        if (r == null) {
            logger.error("could not obtain response from ticket!");
            return null;
        }
        this.tickets.remove(t);
        return r;
    }

    private Ticket getTicket(String number) {
        for (Ticket t : this.tickets) {
            if (number.equals(t.getNumber())) return t;
        }
        return null;
    }

}
