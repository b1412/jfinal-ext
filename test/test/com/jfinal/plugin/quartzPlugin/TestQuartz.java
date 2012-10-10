package test.com.jfinal.plugin.quartzPlugin;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.jfinal.plugin.quartz.QuartzPlugin;

public class TestQuartz {

	@Test
	public void test() throws InterruptedException {
		QuartzPlugin quartzPlugin = new QuartzPlugin("job.properties");
		quartzPlugin.start();
		TimeUnit.SECONDS.sleep(60);
	}

}
