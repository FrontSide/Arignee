package models;

/**
 * Entity Object
 * Stores information about a WebPage
 */

import java.util.List;
import play.db.ebean.Model;
import javax.persistence.Entity;

@Entity
public class WebPage extends Model {

     /* This WebPage's url as fetched */
     public String url;

     /* All links of this page*/
     public List<Hyperlink> hyperlinks;

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
