package com.jfinal.ext.plugin.cron4j;


import com.jfinal.ext.plugin.cron.Cron4jPlugin;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TestCron4j {

    @Test
    public void test() throws InterruptedException {
        Cron4jPlugin cron4jPlugin = new Cron4jPlugin().config("cronjob.properties");
        cron4jPlugin.add("1 * * * *",new JobB());
        cron4jPlugin.start();
        TimeUnit.MINUTES.sleep(2);
    }

}
