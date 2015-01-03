package models.persistency;

/**
 * Entity Object
 * Stores information about a WebPage
 */

import java.util.List;
import java.util.ArrayList;
import play.db.ebean.Model;
import javax.persistence.*;
import play.Logger;
import play.Logger.ALogger;

@Entity
public class WebPage extends Model {

    public WebPage(String url) {
        this.url = url;
    }

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
        return this.url;
    }

    @Override
    public boolean equals(Object o){
        /*TODO*/
        return false;
    }

    /**
     * Returns a List with all the urls from a list of webPages
     * @param  webPages List of webpage Objects
     * @return          List of urls from the passed webPage objects
     */
    public static List<String> getUrls(List<WebPage> webPages) {
        List<String> urls = new ArrayList<>();
        for (WebPage wp : webPages) {
            urls.add(wp.url);
        }
        return urls;
    }

}
