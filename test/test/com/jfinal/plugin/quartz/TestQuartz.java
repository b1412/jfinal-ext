package test.com.jfinal.plugin.quartz;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.jfinal.plugin.quartz.Quartz2Plugin;
public class TestQuartz {
 
	@Test
	public void test() throws InterruptedException {
		Quartz2Plugin quartzPlugin = new Quartz2Plugin("quzrtz2.properties");
		quartzPlugin.start();
		TimeUnit.SECONDS.sleep(20);
		assertEquals(4,JobA.callTime);
		assertEquals(0,JobB.callTime);
	}
	

}
