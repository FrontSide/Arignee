package collectors;

/**
  * Factory for WebsiteHTMLCollectors
  *
  * Singleton
  */

public class WebsiteHtmlCollectorFactory {

    private WebsiteHtmlCollectorFactory(){
    }

    private static final WebsiteHtmlCollectorFactory INSTANCE =
                                    new WebsiteHtmlCollectorFactory();
    public static final WebsiteHtmlCollectorFactory getInstance() {
        return WebsiteHtmlCollectorFactory.INSTANCE;
    }

    public WebsiteHtmlCollector create() {
        return new WebsiteHtmlCollector(new WebsiteHtmlJsoupCollector());
    }

}
