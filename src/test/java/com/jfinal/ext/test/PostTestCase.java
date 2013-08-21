package com.jfinal.ext.test;

import java.io.File;

import org.junit.Test;

public class PostTestCase extends ControllerTestCase<Config> {

    @Test
    public void line() throws Exception {

        String url = "/post";
        String filePath = Thread.currentThread().getContextClassLoader().getResource("dataReq.xml").getFile();
        String fileResp = "/home/kid/git/jfinal-ext/resource/dataResp.xml";
        String resp = use(url).post(new File(filePath)).writeTo(new File(fileResp)).invoke();
        System.out.println(resp);
    }

    @Test
    public void test3() {
        String url = "/post/1?age=1&age=2&name=2";
        String body = "<root>中文</root>";
        use(url).post(body).invoke();
    }
}
