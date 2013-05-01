package com.jfinal.ext.route;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jfinal.ext.test.ControllerTestCase;

public class TestRoute extends ControllerTestCase<Config> {


    @Test
    public void testAController() throws Exception {
        use("/aa").invoke();
        assertEquals("zhoulei", findAttrAfterInvoke("name"));
        assertEquals(24, findAttrAfterInvoke("age"));
    }

    @Test
    public void testBController() throws Exception {
        use("/bb").invoke();
        assertEquals("zhoulei", findAttrAfterInvoke("name"));
        assertEquals(24, findAttrAfterInvoke("age"));
    }

    @Test
    public void testCController() throws Exception {
        use("/c").invoke();
    }
}
