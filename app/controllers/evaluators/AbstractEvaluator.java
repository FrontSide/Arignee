package evaluators;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import play.Logger;
import play.Logger.ALogger;

import models.collection.CollectorValue;
import models.evaluation.EvaluationValue;
import collectors.enums.CollectorKey;
import evaluators.enums.EvaluatorKey;
import evaluators.enums.WebsiteHtmlEvaluatorKey;
import ticketing.TicketProcessor;
import ticketing.TicketHandler;
import ticketing.TicketStatus;

public abstract class AbstractEvaluator implements Evaluator, TicketProcessor {

    private static final ALogger logger = Logger.of(AbstractEvaluator.class);

    //Map from collector
    private Map<? extends CollectorKey, CollectorValue> collected;

    protected EvaluationValue result;

    /* ------------ Impl of TicketProcessor ------------ */
    private final TicketHandler TICKETHANDLER = TicketHandler.getInstance();
    protected String ticketNumber;

    @Override
    public void setTicketNumber(String number) {
        this.ticketNumber = number;
    }

    @Override
    public void updateTicketStatus(TicketStatus status) {
        if (this.ticketNumber == null) return;
        this.TICKETHANDLER.updateStatus(this.ticketNumber, status);
    }
    /* ------------ ----------------------- ------------ */

    public abstract EvaluationValue get();

    public Evaluator pass(Map<? extends CollectorKey, CollectorValue> collected) {
        this.collected = collected;
        return this;
    }

    Map<? extends CollectorKey, CollectorValue> collected() {
        if (collected == null)
            throw new RuntimeException("No Collected Data found!");
        return this.collected;
    }

    /**
      * Calculates the percentual divergence of two values
      * @param final DESIRED : the desired/ideal value which is used as base
      * @param final REAL : the actual value which divergence to the
      *               desired value is calculated
      * @returns : the percentual divergence from the REAL value to the
      *            desired value
      */
    public static float percentualDivergence(final float DESIRED, final float ACTUAL) {
        return (ACTUAL/DESIRED)*100;
    }
    public static float percentualDivergence(final int DESIRED, final int ACTUAL) {
        return percentualDivergence((float) DESIRED, (float) ACTUAL);
    }

}
