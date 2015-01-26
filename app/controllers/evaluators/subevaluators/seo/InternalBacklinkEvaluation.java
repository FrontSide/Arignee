package evaluators.subevaluators.seo;

/**
*
*/

import models.persistency.Hyperlink;
import models.persistency.WebPage;
import collectors.WebsiteHtmlCollectorFactory;
import collectors.WebsiteHtmlCollector;
import url.URLHandler;

import java.util.List;
import java.util.ArrayList;

import play.Logger;
import play.Logger.ALogger;

public class InternalBacklinkEvaluation implements BacklinkEvaluationStrategy {

    private static final ALogger logger = Logger.of(InternalBacklinkEvaluation.class);

    public int getNumberOfPagesWithBacklink(WebPage webPage, List<Hyperlink> links, final WebsiteHtmlCollectorFactory COLLECTORFACTORY) {

        //List with all WebPages that are linked to from the source-Page
        List<WebPage> pagesLinkedTo = new ArrayList<>();

        //List with all Pages that have a backlink to the source-page
        List<WebPage> pagesWithBacklinks = new ArrayList<>();

        //Go through all HREFS on the website to evaluate
        for (Hyperlink h : links) {

            String href = h.href;

            logger.info("browsing URL for backlinks :: " + href);

            //First check if this link just refers to its own page
            if (URLHandler.isARecursiveLink(webPage, href)) continue;

            collectors.Collector collector = COLLECTORFACTORY.create();

            /* Check if href is an url-appendix (parameter, path, #)
            * If so, build a complete url with the url of the eval-WebPage */
            if (URLHandler.isUrlAppendix(href)) {
                href = (href.startsWith("/")) ? href : "/" + href;
                href = webPage.url.getHost() + href;
            }

            WebPage targetWebsite = new WebPage(href);
            logger.debug("done creating.");
            pagesLinkedTo.add(targetWebsite);

            List<Hyperlink> targetWebsiteLinks = null;

            try {
                //Get all the hrefs of the Links from the linked-to page
                targetWebsiteLinks =
                ((WebsiteHtmlCollector) collector.url(href).fetch())
                .getHyperlinks();

                if (targetWebsiteLinks == null) continue; //Skip if no links
            } catch (RuntimeException e) {
                logger.error("Failed to fetch hrefs for \"" + href + "\"");
                logger.warn("skipping iteration...");
                continue;
            }

            targetWebsite.hyperlinks = targetWebsiteLinks;

            //Go through all the hrefs on the Link's target Website
            for (Hyperlink twh : targetWebsiteLinks) {
                if (URLHandler.isARecursiveLink(webPage, twh.href)) {
                    logger.info("URL was identified as backlink :: " + twh.href);
                    pagesWithBacklinks.add(targetWebsite);
                    break; //break if backlink detected
                }
            }

            logger.info("The URL \"" + h + "\" has >= 1 backlinks");

        }

        return pagesWithBacklinks.size();

    }

}
