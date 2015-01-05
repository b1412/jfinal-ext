package com.jfinal.ext.plugin.quartz;


import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TestQuartz {

    @Test
    public void test() throws InterruptedException {
        QuartzPlugin quartzPlugin = new QuartzPlugin("quartzjob.properties","quartz.properties");
        quartzPlugin.add("0 * * * * *",new JobB());
        quartzPlugin.version(QuartzPlugin.VERSION_1);
        quartzPlugin.start();
        TimeUnit.SECONDS.sleep(20);
    }

}
