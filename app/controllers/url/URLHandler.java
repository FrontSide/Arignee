package url;

/**
* Handles the instantiation of URLs (java.net) and
* has static methods to inspect and modify certain attributes of URLs
*
* Singleton
*/

import java.net.URL;
import java.net.MalformedURLException;
import org.apache.commons.lang3.StringUtils;

import models.persistency.WebPage;

import play.Logger;
import play.Logger.ALogger;

public class URLHandler {

    private static final ALogger logger = Logger.of(URLHandler.class);

    private static final String HTTP_PROTOCOL = "http";
    private static final String HTTPS_PROTOCOL = "https";

    private URLHandler(){}
    private static URLHandler instance;
    public static URLHandler getInstance() {
        if (URLHandler.instance == null)
        URLHandler.instance = new URLHandler();
        return URLHandler.instance;
    }

    /*************************************************/

    /**
     * Instantiates a java.net.URL object from a given string
     * (Throws Illegal Argument Exception if not possible to create url)
     * @param  url string to be converted to URL object
     * @return     url object
     */
    public URL create(String url) throws IllegalArgumentException {

        logger.debug("Try creating url from :: " + url);

        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            logger.warn("non-valid url :: " + url + " :: trying to modify...");
        }

        if (!url.matches("(.)*(://)(.)*")) url = HTTP_PROTOCOL + "://" + url;

        logger.debug("After protocol sub :: " + url);

        if (StringUtils.countMatches(url, ".") < 1)
            throw new IllegalArgumentException("No . found in url String");
        else if (StringUtils.countMatches(url, ".") == 1) {
            logger.debug("One . found in :: " + url);
            String[] urlSplit = url.split("://");
            if (urlSplit.length != 2)
                throw new IllegalArgumentException( "cannot create url from :: "
                                                    + url);
            url = urlSplit[0] + "://www." + urlSplit[1];
        }

        logger.debug("checked url-string before shlash reduction is :: " + url);

        //Remove last slash if existing
        if (url.charAt(url.length()-1) == '/') url = url.substring(0, url.length()-1);

        logger.debug("checked url-string is :: " + url);

        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("cannot crate url from :: " + url);
        }

    }

    /**
     * Creates a URL by combining parameters
     * (throws Illegal Argument Exception if no host is given)
     * @param   protocol    e.g. http
     * @param   host        e.g. www.dary.info
     * @param   path        e.g. /blog/ireland
     * @param   query       e.g. ?page=1
     * @param   ref         e.g. #top
     * @return  The assembled URL object
     */
    public URL createFromComponents(    String protocol,
                                        String host,
                                        String path,
                                        String query,
                                        String ref )
                                        throws IllegalArgumentException {

        protocol = (protocol == null) ? protocol = HTTP_PROTOCOL : protocol;
        protocol = (protocol.contains("://")) ? protocol : protocol + "://";
        if (host == null) throw new IllegalArgumentException("No host given!");
        if (StringUtils.countMatches(host, ".") < 1) throw new IllegalArgumentException("Invalid host given!");
        host = (StringUtils.countMatches(host, ".") == 1) ? "www." + host : host;
        path = (path == null) ? "" : path;
        path = (path.startsWith("/")) ? path : "/" + path;
        path = (isIrrelevantURLAppendix(path)) ? "" : path;
        query = (query == null) ? "" : query;
        ref = (ref == null) ? "" : "#" + ref;

        logger.debug("createFrom :: " + protocol + " :: " + host + " :: " + path + " :: " + query + " :: " + ref);

        return this.create(protocol + host + path + query + ref);

    }

    /*************************************************/

    /**
     * swaps a url's protocol from https to http and vice versa
     * @param  url
     * @return     url with swapt http protocol
     */
    public static URL swapSecureHttpProtocolFromUrl(URL url) {
        if (url.getProtocol().equals(HTTP_PROTOCOL))
            return URLHandler.getInstance().create(HTTPS_PROTOCOL + removeProtocol(url));
        return URLHandler.getInstance().create(HTTP_PROTOCOL + removeProtocol(url));
    }

    /**
    * Checks if a URL is within a domain i.e. has the same Base-URL
    * @param  domainUrl    the base-url of the referenced domain
    * @param  URL          the url or path that is checked
    * @return              true if the passed url is considered an
    *                      internal url or merely a path
    */
    public static boolean isInternalUrl(URL domainURL, URL url) {
        return getTrimmedHost(domainURL).equals(getTrimmedHost(url));
    }

    public static boolean isInternalUrl(URL domainURL, String urlS) {
        if (urlS.equals("")) return true;
        if (!urlS.contains(".")) return true;
        if ("#?/".contains(urlS.charAt(0) + "")) return true;
        URL url = URLHandler.getInstance().create(urlS);
        if (url != null) return isInternalUrl(domainURL, url);
        return false;
    }

    /* Overload */
    public static boolean isInternalUrl(WebPage domain, URL url) {
        return isInternalUrl(domain.url, url);
    }

    /* Overload */
    public static boolean isInternalUrl(WebPage domain, String urlS) {
        return isInternalUrl(domain.url, urlS);
    }

    /**
    * Checks if a String is merely a Path, Parameter or
    * Resource Identifier (i.e. #) rather than a full URL
    * @param   s  String to check
    * @returns    true if String is a url- path,
    *             parameter or resource identifier
    */
    public static boolean isUrlAppendix(String s) {
        if (s == null || s.equals("") || !s.contains(".")) return true;
        return "#?/".contains("" + s.charAt(0));
    }

    /**
     * Checks if a URL appendix is irrelevant
     * i.e. does not link to a path and does not contain queries
     * @param  appendix URL appendix
     * @return          true if appendix is irrelevant in URL
     */
    public static boolean isIrrelevantURLAppendix(String appendix) {
        logger.debug("isIrrelevantURLAppendix :: " + appendix);
        appendix = appendix.trim();
        if (!isUrlAppendix(appendix)) return false;
        if (appendix.startsWith("#") || appendix.startsWith("/#") || appendix.equals("/") || appendix.equals("")) {
            logger.debug("true");
            return true;
        }
        return false;
    }

    /**
    * Checks if a Link links to its parent-Page
    * (i.e. the page where it is displayed on)
    * @param   parentPageUrl   The Url of the page the link occurs on
    * @param   linkHref        The Url the hyperlink links to
    * @return  true if the link (linkHref) points to the paren Page(-Url)
    */
    public static boolean isARecursiveLink(String parentPageUrlS, String linkHrefS) {
        logger.debug("isARecursiveLink String String :: " + parentPageUrlS + " :: " + linkHrefS);

        linkHrefS = linkHrefS.trim();
        parentPageUrlS = parentPageUrlS.trim();

        URL parentPageUrl = null;
        URL linkHref = null;

        try {
            parentPageUrl = URLHandler.getInstance().create(parentPageUrlS);
        } catch (IllegalArgumentException e) {
            logger.warn("could not create url from :: " + parentPageUrlS);
        }

        try {
            linkHref = URLHandler.getInstance().create(linkHrefS);
        } catch (IllegalArgumentException e) {
            logger.warn("could not create url from :: " + linkHrefS);
        }

        if (parentPageUrl == null) {

            return (linkHref == null)   ? parentPageUrlS.equals(linkHrefS)
                                        : isARecursiveLink(parentPageUrlS,
                                            removeProtocolAndWWW(linkHref));

        }

        return (linkHref == null)   ? isARecursiveLink(parentPageUrl, linkHrefS)
                                    : isARecursiveLink(parentPageUrl, linkHref);
    }


    /* Overload */
    public static boolean isARecursiveLink(WebPage parentPage, String linkHref) {
        //calls overload URL String
        return isARecursiveLink(parentPage.url, linkHref);
    }

    /* Overload */
    public static boolean isARecursiveLink(URL parentPageUrl, String linkHref) {

        logger.debug("isARecursiveLink URL String :: "
                                    + parentPageUrl + " :: " + linkHref);

        //Try converting linkHref to URL
        try {

            URL url = URLHandler.getInstance().create(linkHref);
            return isARecursiveLink(parentPageUrl, url);

        } catch (IllegalArgumentException e) {
            logger.warn("could not create url from :: " + linkHref);
        }

        //Check if linkHref is merely an URL appendix
        if (isUrlAppendix(linkHref)) {

            logger.debug("isUrlAppendix :: " + linkHref);

            //If it's nothing but an irrelevant URL appendix it is already true
            if (isIrrelevantURLAppendix(linkHref)) return true;

            logger.debug("is NOT IrrelevantURLAppendix :: " + linkHref);

            //Tring preceeding slash
            if (linkHref.startsWith("/"))
                linkHref = linkHref.substring(1, linkHref.length());

            //Try to create URL by adding the linkHref
            //(which we know here is a URL appendix)
            //to the parentPageUrl (without appendix)
            try {

                return isARecursiveLink(parentPageUrl, URLHandler.getInstance()
                                        .createFromComponents(
                                            parentPageUrl.getProtocol(),
                                            getTrimmedHost(parentPageUrl),
                                            "/" +  linkHref, null, null));

            } catch (IllegalArgumentException e) {
                logger.warn("could not create url from :: " + linkHref);
            }
        }

        //Remove protocol and www from parentPage url
        //and call String, String overload
        return removeProtocolAndWWW(parentPageUrl).equals(linkHref);
    }

    /* Overload */
    public static boolean isARecursiveLink(URL parentPageUrl, URL linkHref) {
        logger.debug("isARecursiveLink URL URL :: "
                                    + parentPageUrl + " :: " + linkHref);

        // If URL appendix is irrelevant - remove it from url
        if (isIrrelevantURLAppendix(trimToUrlAppendix(linkHref))) {
            try {

                linkHref = URLHandler.getInstance().createFromComponents(
                                                    linkHref.getProtocol(),
                                                    getTrimmedHost(linkHref),
                                                    null, null, null);

            } catch (IllegalArgumentException e) {
                logger.warn("could not create url from :: " + linkHref);
            }

            logger.debug("isARecursiveLink URL URL after assmb. :: "
                                    + parentPageUrl + " :: " + linkHref);

        }

        //First try String comparison before calling th URL equals
        if (parentPageUrl.toString().equals(linkHref.toString())) return true;

        logger.debug("not same stinrg :: " + parentPageUrl.toString() + " :: " + linkHref.toString());

        //Compare URLs
        return parentPageUrl.equals(linkHref);
    }

    /* Overload */
    public static boolean isARecursiveLink(WebPage parentPage, URL linkHref) {
        //calls overload URL URL
        return isARecursiveLink(parentPage.url, linkHref);
    }

    /**
    * Removes paths, protocols and subdomains from a URL to leave just the base
    * and Top-Level Domain (e.g http://dary.info/blog/bac1 --> dary.info)
    * @param  url url to trim
    * @return     trimmed url
    */
    private static String getTrimmedHost(URL url) {
        String host = url.getHost();
        host = (host.startsWith("www.")) ? host.replaceFirst("www.","") : host;
        host = (host.contains("/")) ? host.substring(0, host.indexOf("/")) : host;
        return host;
    }

    /**
    * Removes the Protocols from a url
    * @param   url url with protocol
    * @return      url withour protocol
    */
    public static String removeProtocol(URL url) {
        logger.debug("prot is :: " + url.getProtocol());
        return url.toString().replaceFirst(url.getProtocol(), "").replace("://", "");
    }

    /**
    * Removes the Protocols from a url and a www subdomain
    * @param   url url with protocol and www.
    * @return      url withour protocol and www.
    */
    public static String removeProtocolAndWWW(URL url) {
        String s = removeProtocol(url);
        return s.startsWith("www.") ? s.replaceFirst("www.","") : s;
    }

    /**
    * Removes protocols, and all domain-levels
    * until only the appendix of a URL (path, parameters, resoucre id.) is left
    * @param  url url to trim
    * @return     url appendix (path, patameter, resource identifiers)
    */
    private static String trimToUrlAppendix(URL url) {
        String path = (url.getPath() == null) ? "" : url.getPath();
        path = (path.startsWith("/")) ? path : "/" + path;
        String query = (url.getQuery() == null) ? "" : url.getQuery();
        String ref = (url.getRef() == null) ? "" : url.getRef();
        return  path + query + "#" + ref;
    }

}
