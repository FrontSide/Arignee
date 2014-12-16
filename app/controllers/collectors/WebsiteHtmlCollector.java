package collectors;

public abstract class WebsiteHtmlCollector<T> implements Collector {

    public abstract T fetchHtml(final String URL);

}
