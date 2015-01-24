import org.junit.*;
import static org.junit.Assert.*;
import play.Logger;
import java.util.List;
import java.util.ArrayList;

import url.URLHandler;

public class IsUrlAppendixTest extends URLHandlerTest {


    @Test
    public void defPositiveTest() {
        assertTrue(this.isUrlAppendixInvoker("/home"));
    }

    @Test
    public void defWithQueryPositiveTest() {
        assertTrue(this.isUrlAppendixInvoker("/home?page=1"));
    }

    @Test
    public void justQueryPositiveTest() {
        assertTrue(this.isUrlAppendixInvoker("?page=1"));
    }

    @Test
    public void justReferencePositiveTest() {
        assertTrue(this.isUrlAppendixInvoker("#position1"));
    }

    @Test
    public void defWithQueryAndReferencePositiveTest() {
        assertTrue(this.isUrlAppendixInvoker("/home/?page=1#position1"));
    }

    @Test
    public void defWithReferencePositiveTest() {
        assertTrue(this.isUrlAppendixInvoker("/home/#position1"));
    }

    @Test
    public void rootPositiveTest() {
        assertTrue(this.isUrlAppendixInvoker("/"));
    }

    @Test
    public void nameOnlyPositiveTest() {
        assertTrue(this.isUrlAppendixInvoker("home"));
    }

    /* --------------------------------------------------- */

    @Test
    public void fullUrlNegativeTest() {
        assertFalse(this.isUrlAppendixInvoker("http://dary.ie"));
    }

    @Test
    public void fullUrlWithPathNegativeTest() {
        assertFalse(this.isUrlAppendixInvoker("http://dary.ie/home"));
    }

    @Test
    public void domainAndQueryNegativeTest() {
        assertFalse(this.isUrlAppendixInvoker("dary.ie?page=1"));
    }

    @Test
    public void domainAndReferenceNegativeTest() {
        assertFalse(this.isUrlAppendixInvoker("dary.ie/#positon"));
    }

    @Test
    public void subDomainNegativeTest() {
        assertFalse(this.isUrlAppendixInvoker("www.dary.ie/home"));
    }

    /* --------------------------------------------------- */

    private boolean isUrlAppendixInvoker(String url) {
        return URLHandler.isUrlAppendix(url);
    }

}
