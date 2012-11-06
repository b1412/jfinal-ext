package com.jfinal.ext.plugin.jms;

import java.io.Serializable;


public class JmsKit {
	private static volatile JmsSender jmsSender;
	
	static void init(JmsSender jmsSender ) {
		JmsKit.jmsSender = jmsSender;
	}
	
	public static boolean sendQueue(String queueName, Serializable message, String msgName) {
		return jmsSender.queueSend(queueName, message, Integer.parseInt(JmsConfig.getVal("queue."+queueName+"."+msgName)));
	}
	
	public static boolean sendTopic(String topicName, Serializable message, String msgName) {
		return jmsSender.topicSend(topicName, message, Integer.parseInt(JmsConfig.getVal("topic."+topicName+"."+msgName)));
	}
}
