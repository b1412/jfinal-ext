package test.com.jfinal.plugin.jms;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.jfinal.plugin.config.ConfigPlugin;
import com.jfinal.plugin.jms.JmsKit;
import com.jfinal.plugin.jms.JmsPlugin;

public class TestJms {
	@Test
	public void sendQueue() throws InterruptedException {
		JmsPlugin jmsPlugin = new JmsPlugin("jms.properties");
		jmsPlugin.start();
		JmsKit.sendQueue("q1", new M(), "a");
		TimeUnit.SECONDS.sleep(60);
	}
	@Test
	public void sendQueue2() throws InterruptedException {
		ConfigPlugin configPlugin = new ConfigPlugin("jms.properties");
		JmsPlugin jmsPlugin = new JmsPlugin(configPlugin);
		configPlugin.start();
		jmsPlugin.start();
		JmsKit.sendQueue("q1", new M(), "a");
		TimeUnit.SECONDS.sleep(60);
	}

}
