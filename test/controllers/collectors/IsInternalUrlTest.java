import org.junit.*;
import static org.junit.Assert.*;
import play.Logger;

import java.util.List;
import java.util.ArrayList;

import models.persistency.WebPage;

import url.URLHandler;

public class IsInternalUrlTest extends URLHandlerTest {

    private final String domainUrl = "www.dary.info";

    @Before
    public void setup() {

    }

    @Test
    public void samePagePositiveTest() {
        assertTrue(this.isInternalUrlInvoker("/"));
    }

    @Test
    public void urlParameterPositiveTest() {
        assertTrue(this.isInternalUrlInvoker("?page=2"));
    }

    @Test
    public void urlPathPositiveTest() {
        assertTrue(this.isInternalUrlInvoker("/blog/ireland"));
    }

    @Test
    public void fragmentIdentifierPositiveTest() {
        assertTrue(this.isInternalUrlInvoker("#loginform"));
    }

    @Test
    public void protocolNoSubdomainPositiveTest() {
        assertTrue(this.isInternalUrlInvoker("http://dary.info"));
    }

    @Test
    public void protocolWithSubdomainPositiveTest() {
        assertTrue(this.isInternalUrlInvoker("http://www.dary.info"));
    }

    /* --------------------------------------------------- */

    @Test
    public void differentDomain1NegativeTest() {
        assertFalse(this.isInternalUrlInvoker("www.google.ie"));
    }

    @Test
    public void differentDomain2NegativeTest() {
        assertFalse(this.isInternalUrlInvoker("www.google.info"));
    }

    @Test
    public void differentTopLevel1NegativeTest() {
        assertFalse(this.isInternalUrlInvoker("dary.ie"));
    }

    @Test
    public void differentTopLevel2NegativeTest() {
        assertFalse(this.isInternalUrlInvoker("http://dary.ie"));
    }

    @Test
    public void subdomainNegativeTest() {
        assertFalse(this.isInternalUrlInvoker("subdomain.dary.info"));
    }

    @Test
    public void subdomainWithPathNegativeTest() {
        assertFalse(this.isInternalUrlInvoker("subdomain.dary.info/blog/cuisine"));
    }


    private boolean isInternalUrlInvoker(String url) {
        return URLHandler.isInternalUrl(new WebPage(this.domainUrl), url);
    }

}
