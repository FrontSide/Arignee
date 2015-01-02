package collectors;

/**
 *
 */

import java.util.Map;
import collectors.enums.CollectorKey;
import models.collection.CollectorValue;

public interface GoogleTrendsCollectorStrategy {

    /**
    * Extract collector data from raw GoogleTrends response
    * served by HTTPRequestor
    * @return     Collector Data in Maps
    */
    Map<? extends CollectorKey, CollectorValue> extractFromRaw();

}
