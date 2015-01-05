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

    public final long MIN = 1000L;
    private long lastTicketNumber = 999L; //Fist ticket has number 1000
    public long getNextTicketNumber() {
        return ++this.lastTicketNumber;
    }

    public long getNewTicket() {
        Ticket t = new Ticket();
        this.tickets.add(t);
        return t.getNumber();
    }

    public void updateStatus(long number, TicketStatus status) {
        logger.info("updating status of ticket :: "
                    + number + " :: to :: " + status);
        this.getTicket(number).setStatus(status);
    }

    /**
     * Mark a Ticket as finished and ready to have result obtained
     * @param number number of ticket to mark as finished
     */
    public void markAsFinished(long number) {
        this.getTicket(number).setFinished();
    }

    /**
     * Passes a promise of a Result to a ticket
     * @param  number   number of the ticket to pass result to
     * @param  result   Promise of the result
     */
    public void passResponse(long number, Promise<Result> result) {
        this.getTicket(number).setResponse(result);
    }

    public TicketStatus getStatus(long number) {

        Ticket t = getTicket(number);

        if (t == null) {
            if (number <= this.lastTicketNumber) return TicketStatus.TICKET_DELETED;
            return TicketStatus.TICKET_NOT_FOUND;
        }

        return t.getStatus();
    }

    public Result getResponse(long number) {
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

    private Ticket getTicket(long number) {
        for (Ticket t : this.tickets) {
            if (t.getNumber() == number) return t;
        }
        return null;
    }

}
