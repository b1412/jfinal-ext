package test.com.jfinal.ext.plugin.quartz;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.jfinal.ext.plugin.quartz.QuartzPlugin;

public class TestQuartz {

    @Test
    public void test() throws InterruptedException {
        QuartzPlugin quartzPlugin = new QuartzPlugin("quartzjob.properties");
        quartzPlugin.start();
        TimeUnit.SECONDS.sleep(20);
        assertEquals(4, JobA.callTime);
        assertEquals(0, JobB.callTime);
    }

}
