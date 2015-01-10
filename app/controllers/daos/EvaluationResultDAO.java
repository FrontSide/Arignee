package daos;

/**
*
*/

import models.persistency.EvaluationResult;
import models.persistency.Hyperlink;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import play.Logger;
import play.Logger.ALogger;

public class EvaluationResultDAO implements DAO<EvaluationResult> {

    private static final ALogger logger = Logger.of(EvaluationResultDAO.class);

    private static Finder<Long,EvaluationResult> find =
    new Finder<>(Long.class, EvaluationResult.class);

    @Override
    public void save(EvaluationResult model) {
        logger.info("persisting model :: " + model);
        Ebean.save(model);
    }

    public EvaluationResult getByTicketNumber(String ticketNumber) {
        return this.find.where().eq("ticketNumber", ticketNumber).findUnique();
    }

    public boolean isResultForTicketAvailable(String ticketNumber) {

        /* Check if entered String does not contain any harmful characters
         * and resembles valid UUID */
        if (!ticketNumber.matches("(\\w)*[-](\\w)*[-](\\w)*[-](\\w)*[-](\\w)*")) return false;

        String sql = "select count(ticket_number) as count from evaluation_result where ticket_number LIKE '" + ticketNumber + "'";
        SqlRow row = Ebean.createSqlQuery(sql).findUnique();
        return row.getInteger("count") == 1;
    }

    @Override
    public EvaluationResult getById(long id) {
        return null;
    }

    @Override
    public void remove(EvaluationResult model) {

    }

    @Override
    public void update(EvaluationResult model) {
        throw new UnsupportedOperationException("Evaluation Results may not be changed once persisted!");
    }

}
