package com.jfinal.ext.render.dwz;

import org.junit.Test;

import com.jfinal.ext.test.ControllerTestCase;

public class TestDwzRender extends ControllerTestCase<DwzConfig> {

    @Test
    public void testDeleteError() throws Exception {
        use("/dwz").invoke();
    }

    @Test
    public void testDeleteSuccess() throws Exception {
        use("/dwz/delete/2?age=1").invoke();
    }
}
