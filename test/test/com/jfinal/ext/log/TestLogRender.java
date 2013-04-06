package test.com.jfinal.ext.log;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jfinal.ext.test.ControllerTestCase;

public class TestLogRender extends ControllerTestCase {
    @BeforeClass
    public static void init() throws Exception {
        start(LogConfig.class);
    }

    @Test
    public void test() throws Exception {
        invoke("/");
    }
}
