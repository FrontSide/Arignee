package collectors;

public class GoogleTrendsCollectorFactory implements CollectorFactory<GoogleTrendsCollector> {

    public GoogleTrendsCollector create() {
        return new GoogleTrendsElibusCollector();
    }

}
