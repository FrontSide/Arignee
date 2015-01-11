package daos;

/**
*
*/

import java.util.List;
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

        String sql = "SELECT COUNT(ticket_number) AS count FROM evaluation_result WHERE ticket_number LIKE '" + ticketNumber + "'";
        SqlRow row = Ebean.createSqlQuery(sql).findUnique();
        return row.getInteger("count") == 1;
    }

    /**
     * Retrains all the Evaluation Results with a matching webPage foreign key
     * @param  webPageId Id of webpage to fetch eval results for
     * @return           List of Evaluation Results for webPage
     */
    public List<EvaluationResult> getByWebPageId(int webPageId) {
        return this.find.where().eq("web_page_id", webPageId).findList();
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
