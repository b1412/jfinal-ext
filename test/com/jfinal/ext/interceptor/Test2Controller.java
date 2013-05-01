package com.jfinal.ext.interceptor;

import java.util.concurrent.TimeUnit;

import com.jfinal.core.Controller;

public class Test2Controller extends Controller {
    public void a() {
        throw new IllegalArgumentException();
    }

    public void b() {
        throw new IllegalStateException();
    }
    public void c() {
        int i = 1/0;
        renderText(i+"");
    }

    public void d() {
        render("d.html");
    }

    public void test() throws InterruptedException {
        TimeUnit.SECONDS.sleep(30);
        setAttr("age", 1);
        renderFreeMarker("test.html");
    }
}
