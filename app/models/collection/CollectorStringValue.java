package models.collection;

import java.util.List;

public class CollectorStringValue extends CollectorValue<String> {

    public CollectorStringValue() {}

    public CollectorStringValue(List<String> list) {
        this.add(list);
    }

    public CollectorStringValue(String value) {
        this.add(value);
    }

}
