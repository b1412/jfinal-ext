package com.jfinal.ext.plugin.config;

import com.jfinal.log.Logger;
import org.junit.BeforeClass;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class TestConfig {
    @BeforeClass
    public static void init() {
        Logger.init();
        ConfigPlugin configPlugin = new ConfigPlugin(".*.txt").reload(false);
        configPlugin.start();
    }

    //@Test
    public void testGetStr() throws InterruptedException {
        assertEquals("test", ConfigKit.getStr("name"));
        assertEquals(1, ConfigKit.getInt("age"));
        TimeUnit.SECONDS.sleep(30);
        assertEquals(1, ConfigKit.getInt("age"));
    }

    //@Test
    public void testZw() throws InterruptedException {
        assertEquals("中文内容", ConfigKit.getStr("zw"));
        assertEquals("xxx", ConfigKit.getStr("中"));
    }

}
