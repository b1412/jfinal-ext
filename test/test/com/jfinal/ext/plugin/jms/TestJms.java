package test.com.jfinal.ext.plugin.jms;

//import static org.mockito.Matchers.any;
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.when;
//import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;

import com.jfinal.ext.plugin.config.ConfigPlugin;
import com.jfinal.ext.plugin.jms.JmsKit;
import com.jfinal.ext.plugin.jms.JmsPlugin;
//@RunWith(PowerMockRunner.class)
//@PrepareForTest(JmsKit.class)

public class TestJms {
    @Test
    public void sendQueue() throws InterruptedException {
        // mockStatic(JmsKit.class);
        // when(JmsKit.sendQueue(anyString(), any(M.class), anyString()))
        // .thenReturn(true);
        JmsPlugin jmsPlugin = new JmsPlugin("jms.properties");
        jmsPlugin.start();
        JmsKit.sendQueue("q1", new M(), "a");
        TimeUnit.SECONDS.sleep(60);
    }

    @Ignore
    @Test
    public void sendQueueWithConfigPlugin() throws InterruptedException {
        ConfigPlugin configPlugin = new ConfigPlugin("jms.properties");
        JmsPlugin jmsPlugin = new JmsPlugin(configPlugin);
        configPlugin.start();
        jmsPlugin.start();
        JmsKit.sendQueue("q1", new M(), "a");
        TimeUnit.SECONDS.sleep(60);
    }

}
