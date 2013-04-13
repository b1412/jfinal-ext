package com.jfinal.ext.plugin.jms;

import java.io.Serializable;

public class JmsKit {
    private static JmsSender jmsSender;

    static void init(JmsSender jmsSender) {
        JmsKit.jmsSender = jmsSender;
    }

    public static boolean sendQueue(String queueName, Serializable message, String msgName) {
        return jmsSender.queueSend(queueName, message, JmsConfig.getInt("queue." + queueName + "." + msgName));
    }

    public static boolean sendTopic(String topicName, Serializable message, String msgName) {
        return jmsSender.topicSend(topicName, message, JmsConfig.getInt("topic." + topicName + "." + msgName));
    }
}
