package com.jfinal.ext.plugin.config;

import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jfinal.log.Logger;

public class TestConfig {
    @BeforeClass
    public static void init() {
        Logger.init();
        ConfigPlugin configPlugin = new ConfigPlugin();
        configPlugin.addResource(".*.txt");
        configPlugin.setReload(false);
        configPlugin.start();
    }

    @Test
    public void testGetStr() throws InterruptedException {
        Assert.assertEquals("test", ConfigKit.getStr("name"));
        Assert.assertEquals(1, ConfigKit.getInt("age"));
        TimeUnit.SECONDS.sleep(30);
        Assert.assertEquals(1, ConfigKit.getInt("age"));
    }

    @Test
    public void testZw() throws InterruptedException {
        Assert.assertEquals("中文内容", ConfigKit.getStr("zw"));
        Assert.assertEquals("xxx", ConfigKit.getStr("中"));
    }

}
