package test.com.jfinal.ext.route;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jfinal.ext.test.ControllerTestCase;

public class TestRoute extends ControllerTestCase {

    @BeforeClass
    public static void init() throws Exception {
        start(Config.class);
    }

    @Test
    public void testAController() throws Exception {
        invoke("/aa");
        assertEquals("zhoulei", findAttrAfterInvoke("name"));
        assertEquals(24, findAttrAfterInvoke("age"));
    }

    @Test
    public void testBController() throws Exception {
        invoke("/bb");
        assertEquals("zhoulei", findAttrAfterInvoke("name"));
        assertEquals(24, findAttrAfterInvoke("age"));
    }

    @Test
    public void testCController() throws Exception {
        invoke("/c");
    }
}
