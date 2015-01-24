package models.persistency;

/**
 * Entity Object
 * Stores a Hyperlink with all its attributes and
 * Relations
 */

import play.db.ebean.Model;
import play.data.validation.Constraints.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Hyperlink extends Model {

    @Id
    public long id;

    /* The Webpage this Link is embedded in */
    @Required
    @ManyToOne
    public WebPage parentPage;

    /* The WebPage this hyperlink points to
     * (if it points to a WebPage at all) */
    public WebPage targetPage;

    /* The value of this Link's href Attribute */
    @Required
    public String href;

    /* The Link's Text as it is seen on the Webpage */
    public String text;

    @Override
    public String toString(){
        return "<a " + "href=\"" + href + "\"> " + text + " </a>";
    }

    /**
    * Checks if a Hyperlink links to the same webpage as this one
    * @param   link    Hyperlink to compare
    * @return  true if both hyperlnks point to the same webpage
    */
    public boolean pointsToSameWebPage(Hyperlink link) {
        return this.targetPage.equals(link.targetPage);
    }

    /**
    * Returns a List with all the urls/hrefs from a list of hyperlinks
    * @param  hyperlinks    List of hyperlink Objects
    * @return               List of urls/hrefs from the passed hyperlink objects
    */
    public static List<String> getHrefs(List<Hyperlink> hyperlinks) {
        List<String> urls = new ArrayList<>();
        for (Hyperlink h : hyperlinks) {
            urls.add(h.href);
        }
        return urls;
    }

}
