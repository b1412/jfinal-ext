package com.jfinal.ext.plugin.cron4j;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.jfinal.ext.plugin.cron.Cron4jPlugin;

public class TestCron4j {

    @Test
    public void test() throws InterruptedException {
        Cron4jPlugin cron4jPlugin = new Cron4jPlugin().config("cronjob.properties");
        cron4jPlugin.start();
        TimeUnit.MINUTES.sleep(2);
    }

}
