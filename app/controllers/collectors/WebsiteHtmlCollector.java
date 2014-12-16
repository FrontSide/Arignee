package collectors;

public abstract class WebsiteHtmlCollector implements Collector {

    public abstract String fetchHtml(final String URL);

}
