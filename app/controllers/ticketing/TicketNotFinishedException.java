package ticketing;

/**
 * Runtime Exception that is thrown if a Ticket's response
 * is trying to be obtained before its available i.e. before the
 * Ticket isFinished()
 */

public class TicketNotFinishedException extends RuntimeException {

    TicketNotFinishedException() {
    }

    TicketNotFinishedException(String msg) {
        super(msg);
    }

}
