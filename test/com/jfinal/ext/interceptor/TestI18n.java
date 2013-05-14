package com.jfinal.ext.interceptor;

import org.junit.Test;

import com.jfinal.ext.test.ControllerTestCase;

public class TestI18n extends ControllerTestCase<Config> {
    @Test
    public void test1(){
        use("/p").invoke();
    }
    @Test
    public void test2(){
        use("/p?language=zh&country=cn").invoke();
    }
    @Test
    public void test3(){
        use("/p?language=en").invoke();
    }
    @Test
    public void test4(){
        use("/p?language=tw").invoke();
    }
}
