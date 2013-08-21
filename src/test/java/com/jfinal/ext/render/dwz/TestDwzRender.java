package com.jfinal.ext.render.dwz;

import junit.framework.Assert;

import org.junit.Test;

import com.jfinal.ext.test.ControllerTestCase;

public class TestDwzRender extends ControllerTestCase<DwzConfig> {

    @Test
    public void testDeleteError() throws Exception {
        use("/dwz").invoke();
    }

    @Test
    public void testDeleteSuccess() throws Exception {
        use("/dwz/delete?id=1").invoke();
    }
    @Test
    public void testAddSuccess() throws Exception {
        use("/dwz/add?id=1").invoke();
    }
    
    
    @Test
    public void test1() throws Exception {
        int i = 0;
        int j = 1;
        Assert.assertEquals(10000, add(i, j));
    }

    private int add(int i, int j) {
        return i+j;
    }
    
}
