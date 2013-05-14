package com.jfinal.ext.interceptor;

import com.jfinal.core.Controller;

public class TestController extends Controller {
    public void index(){
        
    }
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

}
