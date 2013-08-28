package com.jfinal.ext.plugin.jms;


import java.util.concurrent.TimeUnit;

import com.jfinal.ext.plugin.config.ConfigPlugin;

public class TestJms {
    ////@Test
    public void sendQueue() throws InterruptedException {
        JmsPlugin jmsPlugin = new JmsPlugin("jms.properties");
        jmsPlugin.start();
        JmsKit.sendQueue("q1", new M(), "a");
        TimeUnit.SECONDS.sleep(60);
    }

   // //@Test
    public void sendQueueWithConfigPlugin() throws InterruptedException {
        ConfigPlugin configPlugin = new ConfigPlugin("jms.properties");
        JmsPlugin jmsPlugin = new JmsPlugin(configPlugin);
        configPlugin.start();
        jmsPlugin.start();
        JmsKit.sendQueue("q1", new M(), "a");
        TimeUnit.SECONDS.sleep(60);
    }

}
