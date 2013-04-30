package com.jfinal.ext.interceptor;

import java.util.concurrent.TimeUnit;

import com.jfinal.core.Controller;

public class TestController extends Controller {
    public void a() {
        throw new IllegalArgumentException();
    }

    public void b() {
        throw new IllegalStateException();
    }

    public void c() {
        render("c.html");
    }

    public void test() throws InterruptedException {
        TimeUnit.SECONDS.sleep(30);
        setAttr("age", 1);
        renderFreeMarker("test.html");
    }
}
