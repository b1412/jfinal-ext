package com.jfinal.plugin.jms;

import java.io.Serializable;
import java.util.Map;

public class JmsSender {
	Map<String, QueueProducer> queueProducers;
	Map<String, TopicPublisher> topicPublishers;

	public boolean queueSend(String queueName, Serializable message, int msgType) {

		QueueProducer queueProducer = queueProducers.get(queueName);
		System.out.println("send msg "+message+"to queue"+queueName);
		return queueProducer.sendMessage(message, msgType);

	}

	public boolean topicSend(String topicName, Serializable message, int msgType) {

		TopicPublisher topicPublisher = topicPublishers.get(topicName);
		System.out.println("send msg "+message+"to topic"+topicName);
		return topicPublisher.publishMessage(message, msgType);

	}
}
