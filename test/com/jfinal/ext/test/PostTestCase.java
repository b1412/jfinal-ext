package com.jfinal.ext.test;

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
        String fileResp = "/home/kid/git/jfinal-ext/test/test/com/jfinal/ext/test/xx2.xml";
        File file = new File(filePath);
        String resp = invoke(url, file, new File(fileResp));
        System.out.println("resp:" + resp);
    }

    @Test
    public void test2() throws Exception {
        String url = "/post";
        String body = "<root>中文</root>";
        invoke(url, body);
    }

    @Test
    public void test3() {
        String url = "/post/1?age=1&age=2&name=2";
        String body = "<root>中文</root>";
        invoke(url, body);

    }
}
