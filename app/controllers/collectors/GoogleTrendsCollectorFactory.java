package collectors;

public class GoogleTrendsCollectorFactory {

    private GoogleTrendsCollectorFactory() {}

    private static final GoogleTrendsCollectorFactory INSTANCE =
                                    new GoogleTrendsCollectorFactory();
    public static final GoogleTrendsCollectorFactory getInstance() {
        return GoogleTrendsCollectorFactory.INSTANCE;
    }

    public GoogleTrendsCollector create() {
        return new GoogleTrendsCollector(new GoogleTrendsElibusCollector());
    }

}
