package models.collection;

import play.db.ebean.Model;
import java.util.List;

public class CollectorModelValue<T extends Model> extends CollectorValue<T> {

    public CollectorModelValue() {}

    public CollectorModelValue(List<T> list) {
        this.add(list);
    }

    public CollectorModelValue(T value) {
        this.add(value);
    }

}
