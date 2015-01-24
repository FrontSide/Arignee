import org.junit.*;
import static org.junit.Assert.*;
import play.Logger;
import java.util.List;
import java.util.ArrayList;

import models.persistency.WebPage;

import url.URLHandler;

public class IsARecursiveLinkTest extends URLHandlerTest {

    @Before
    public void setup() {

    }

    @Test
    public void sameStringPositiveTest() {
        assertTrue(this.isARecursiveLinkInvoker("www.dary.info", "www.dary.info"));
    }

    @Test
    public void protocolPositiveTestA() {
        assertTrue(this.isARecursiveLinkInvoker("www.dary.info", "http://www.dary.info"));
    }

    @Test
    public void protocolPositiveTestB() {
        assertTrue(this.isARecursiveLinkInvoker("www.dary.info/blog/ireland", "http://www.dary.info/blog/ireland"));
    }

    @Test
    public void noProtocolPositiveTest() {
        assertTrue(this.isARecursiveLinkInvoker("http://fm4.orf.at", "fm4.orf.at"));
    }

    @Test
    public void noSubdomainPositiveTestA() {
        assertTrue(this.isARecursiveLinkInvoker("http://www.dary.info", "dary.info"));
    }

    @Test
    public void noSubdomainPositiveTestB() {
        assertTrue(this.isARecursiveLinkInvoker("http://www.dary.info", "http://dary.info"));
    }

    @Test
    public void slashPositiveTest() {
        assertTrue(this.isARecursiveLinkInvoker("www.dary.info", "www.dary.info/"));
    }

    @Test
    public void identifierPositiveTest() {
        assertTrue(this.isARecursiveLinkInvoker("www.dary.info", "www.dary.info/#"));
    }

    @Test
    public void identifierOnlyPositiveTest() {
        assertTrue(this.isARecursiveLinkInvoker("www.dary.info", "#"));
    }

    @Test
    public void identifierOnlyWithIdPositiveTest() {
        assertTrue(this.isARecursiveLinkInvoker("www.dary.info", "#resource"));
    }

    @Test
    public void slashAndIdentifierPositiveTest() {
        assertTrue(this.isARecursiveLinkInvoker("www.dary.info", "/#"));
    }

    @Test
    public void slashAndIdentifierWithIdPositiveTest() {
        assertTrue(this.isARecursiveLinkInvoker("www.dary.info", "/#resource"));
    }

    @Test
    public void slashOnlyPositiveTest() {
        assertTrue(this.isARecursiveLinkInvoker("www.dary.info", "/"));
    }

    @Test
    public void emptyHrefPositiveTest() {
        assertTrue(this.isARecursiveLinkInvoker("www.dary.info", ""));
    }

    /* --------------------------------------------------- */

    @Test
    public void differentTopLevelNegativeTest() {
        assertFalse(this.isARecursiveLinkInvoker("http://www.dary.info", "http://www.dary.at"));
    }

    @Test
    public void differentSubDomainNegativeTest() {
        assertFalse(this.isARecursiveLinkInvoker("fm4.orf.at", "www.orf.at"));
    }

    @Test
    public void differentPathNegativeTest() {
        assertFalse(this.isARecursiveLinkInvoker("dary.info", "dary.info/blog/ireland"));
    }

    @Test
    public void parametersNegativeTest() {
        assertFalse(this.isARecursiveLinkInvoker("dary.info", "dary.info?page=1"));
    }

    @Test
    public void differentPathOnlyNegativeTest() {
        assertFalse(this.isARecursiveLinkInvoker("dary.info", "/path/to"));
    }


    /* --------------------------------------------------- */

    private boolean isARecursiveLinkInvoker(String urla, String urlb) {
        return URLHandler.isARecursiveLink(new WebPage(urla), urlb);
    }

}
