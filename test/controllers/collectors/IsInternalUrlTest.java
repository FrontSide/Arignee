import org.junit.*;
import static org.junit.Assert.*;
import play.Logger;
import java.util.List;
import java.util.ArrayList;

import collectors.AbstractCollector;

public class IsInternalUrlTest extends AbstractCollectorTest {

    private final String domainUrl = "www.dary.info";
    private final List<String> externalUrls = new ArrayList<>();
    private final List<String> internalUrls = new ArrayList<>();

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
    public void subdomainPositiveTest() {
        assertTrue(this.isInternalUrlInvoker("subdomain.dary.info"));
    }
    
    @Test
    public void subdomainWithPathPositiveTest() {
        assertTrue(this.isInternalUrlInvoker("subdomain.dary.info/blog/cuisine"));
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
            
       
    private boolean isInternalUrlInvoker(String url) {
        return AbstractCollector.isInternalUrl(this.domainUrl, url);
    }

}
