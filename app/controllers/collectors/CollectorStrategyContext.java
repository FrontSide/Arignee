package collectors;

/**
 * An interface for Classes that are used as the context of a
 * Strategy Design Pattern i.e. the objects that hold the actual
 * CollectorStrategy
 */

import java.util.Map;
import collectors.enums.CollectorKey;
import models.collection.CollectorValue;

public interface CollectorStrategyContext {

    /**
     * Triggers the executeXX() method of the concrete Strategy
     * @return Map of Collection Results
     */
    public Map<? extends CollectorKey, CollectorValue> executeExtract();

}
