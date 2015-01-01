package models.persistency;

/**
 * Entity Object
 * Stores information about a WebPage
 */

import java.util.List;
import play.db.ebean.Model;
import javax.persistence.*;

@Entity
public class WebPage extends Model {

    public WebPage(String url, EvaluationResult evaluation) {
        this.url = url;
        this.evaluation = evaluation;
    }

    @Id
    public long id;

    /* This WebPage's url as fetched */
    public String url;

    /* All links of this page*/
    public List<Hyperlink> hyperlinks;

    /* Stores the full Map of Evaluation-Results (incl. Keys)
    * in String form.
    */
    public EvaluationResult evaluation;

    @Override
    public String toString(){
        /*TODO*/
        return "";
    }

    @Override
    public boolean equals(Object o){
        /*TODO*/
        return false;
    }

}
