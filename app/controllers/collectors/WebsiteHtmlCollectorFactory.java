package collectors;

public class WebsiteHtmlCollectorFactory implements CollectorFactory<WebsiteHtmlCollector> {

    public WebsiteHtmlCollector create() {
        return new WebsiteHtmlJsoupCollector();
    }

}
