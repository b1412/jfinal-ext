package test.com.jfinal.plugin.jms;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jfinal.plugin.jms.JmsKit;
import com.jfinal.plugin.jms.JmsPlugin;

public class TestJms {
	@BeforeClass
	public static void init() {
		JmsPlugin jmsPlugin = new JmsPlugin();
		jmsPlugin.start();
	}

	@Test
	public void sendQueue() throws InterruptedException {
		JmsKit.sendQueue("q1", new M(), "a");
		TimeUnit.SECONDS.sleep(60);
	}

}

class M implements Serializable {

	private static final long serialVersionUID = 1L;

}
