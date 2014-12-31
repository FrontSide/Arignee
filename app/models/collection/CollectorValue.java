package models.collection;

/**
 * Super-Class for Collector Value
 * that are stored in the Result-Map of a Collector next to the CollectorKey
 *
 * Generic T takes type of object that should be stored as value
 * depends on Subclass
 *
 * A CollectorValue can now either be an Object of type T or a List
 * with objects of type T
 */

import java.util.List;
import java.util.ArrayList;
import play.Logger;

public abstract class CollectorValue<T> {

    protected List<T> list = new ArrayList<>();

    /**
    * Adds a value either to a list of it's type
    * or a single value to its type if none is existing yet
    * @param  value value to add
    * @return       this
    */
    public CollectorValue add(T value) {
        this.list.add(value);
        return this;
    }

    public CollectorValue add(List<T> list) {
        this.list.addAll(list);
        return this;
    }

    /**
     * Get the single object stored in attribute "value" if available
     * @return single value of type T
     */
    public T getValue() {

        if (this.list.size() == 1) return this.list.get(0);
        if (this.list.size() > 1) Logger.error("Only list availabe :: getList()");
        Logger.error("No value avaiable!");
        return null;

    }

    /**
     * Get full list
     * @return List of all stored "values"
     */
    public List<T> getList() {
        return this.list;
    }

    @Override
    public String toString() {
        return this.getList().toString();
    }

}
