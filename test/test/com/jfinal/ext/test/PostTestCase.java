package test.com.jfinal.ext.test;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jfinal.ext.test.ControllerTestCase;

public class PostTestCase extends ControllerTestCase {
    @BeforeClass
    public static void init() throws Exception {
        start(Config.class);
    }

    @Test
    public void test() throws Exception {
        String url = "/post";
        String filePath = Thread.currentThread().getContextClassLoader().getResource("data.xml").getFile();
        File file = new File(filePath);
        invoke(url, file);
    }

    @Test
    public void test2() throws Exception {
        String url = "/post";
        String body = "<root>中文</root>";
        invoke(url, body);
    }
}
