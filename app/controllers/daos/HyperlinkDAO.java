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

    @Override
    public void save(Hyperlink model) {
        logger.info("persisting model :: " + model);
        Ebean.save(model);
    }

    @Override
    public Hyperlink getById(long id) {
        return null;
    }

    @Override
    public void remove(Hyperlink model) {

    }

    @Override
    public void update(Hyperlink model) {
        Ebean.update(model);
    }

}
