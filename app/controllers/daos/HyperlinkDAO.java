package daos;

/**
 *
 */

import models.persistency.Hyperlink;
import com.avaje.ebean.Ebean;
import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import play.Logger;
import play.Logger.ALogger;

public class HyperlinkDAO implements DAO<Hyperlink> {

    private static final ALogger logger = Logger.of(WebPageDAO.class);

    private static Finder<Long,Hyperlink> find =
        new Finder<>(Long.class, Hyperlink.class);

    public void save(Hyperlink model) {
        logger.info("persisting model :: " + model);
        Ebean.save(model);
    }

    public Hyperlink getById(long id) {
        return null;
    }

    public void remove(Hyperlink model) {}

}
