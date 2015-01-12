import org.junit.*;
import static org.junit.Assert.*;
import play.Logger;
import java.util.List;
import java.util.ArrayList;

import collectors.AbstractCollector;

public class PointsToSameWebPageTest extends AbstractCollectorTest {

    @Before
    public void setup() {

    }

    @Test
    public void sameStringPositiveTest() {
        assertTrue(this.pointsToSameWebPageInvoker("www.dary.info", "www.dary.info"));
    }

    @Test
    public void protocolPositiveTestA() {
        assertTrue(this.pointsToSameWebPageInvoker("www.dary.info", "http://www.dary.info"));
    }

    @Test
    public void protocolPositiveTestB() {
        assertTrue(this.pointsToSameWebPageInvoker("www.dary.info/blog/ireland", "http://www.dary.info/blog/ireland"));
    }

    @Test
    public void noProtocolPositiveTest() {
        assertTrue(this.pointsToSameWebPageInvoker("http://fm4.orf.at", "fm4.orf.at"));
    }

    @Test
    public void noSubdomainPositiveTestA() {
        assertTrue(this.pointsToSameWebPageInvoker("http://www.dary.info", "dary.info"));
    }

    @Test
    public void noSubdomainPositiveTestB() {
        assertTrue(this.pointsToSameWebPageInvoker("http://www.dary.info", "http://dary.info"));
    }

    @Test
    public void slashPositiveTest() {
        assertTrue(this.pointsToSameWebPageInvoker("www.dary.info", "www.dary.info/"));
    }

    @Test
    public void identifierPositiveTest() {
        assertTrue(this.pointsToSameWebPageInvoker("www.dary.info", "www.dary.info/#"));
    }

    /* --------------------------------------------------- */

    @Test
    public void differentTopLevelNegativeTest() {
        assertFalse(this.pointsToSameWebPageInvoker("http://www.dary.info", "http://www.dary.at"));
    }

    @Test
    public void differentSubDomainNegativeTest() {
        assertFalse(this.pointsToSameWebPageInvoker("fm4.orf.at", "www.orf.at"));
    }

    @Test
    public void differentPathNegativeTest() {
        assertFalse(this.pointsToSameWebPageInvoker("dary.info", "dary.info/blog/ireland"));
    }


    /* --------------------------------------------------- */

    private boolean pointsToSameWebPageInvoker(String urla, String urlb) {
        return AbstractCollector.pointsToSameWebpage(urla, urlb);
    }

}
