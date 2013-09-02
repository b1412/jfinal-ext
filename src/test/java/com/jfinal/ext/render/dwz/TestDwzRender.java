package com.jfinal.ext.render.dwz;

import com.jfinal.ext.test.ControllerTestCase;

public class TestDwzRender extends ControllerTestCase<DwzConfig> {

    //@Test
    public void testAddSuccess() throws Exception {
        use("/dwz/add?id=1").invoke();
    }

    //@Test
    public void testDeleteError() throws Exception {
        use("/dwz").invoke();
    }

    //@Test
    public void testDeleteSuccess() throws Exception {
        use("/dwz/delete?id=1").invoke();
    }

}
