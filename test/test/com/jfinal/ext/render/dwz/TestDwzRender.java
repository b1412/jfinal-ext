package test.com.jfinal.ext.render.dwz;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jfinal.ext.test.ControllerTestCase;

public class TestDwzRender extends ControllerTestCase {
    @BeforeClass
    public static void init() throws Exception {
        start(DwzConfig.class);
    }

    @Test
    public void testDeleteError() throws Exception {
        invoke("/dwz");
    }

    @Test
    public void testDeleteSuccess() throws Exception {
        invoke("/dwz/delete/2?age=1");
    }
}
