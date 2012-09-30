package com.jfinal.plugin.jms;

import java.io.Serializable;
import java.util.Map;

public class JmsSender {
	Map<String, QueueProducer> queueProducers;
	Map<String, TopicPublisher> topicPublishers;

	public boolean queueSend(String queueName, Serializable message, int msgType) {
		if(queueProducers==null){
			System.err.println("JmsPlugin not start");
		}
		QueueProducer queueProducer = queueProducers.get(queueName);
		System.out.println("send msg "+message+"to queue "+queueName+" ,msgType "+msgType);
		return queueProducer.sendMessage(message, msgType);

	}

	public boolean topicSend(String topicName, Serializable message, int msgType) {
		if(topicPublishers==null){
			System.err.println("JmsPlugin not start");
		}

		TopicPublisher topicPublisher = topicPublishers.get(topicName);
		System.out.println("send msg "+message+"to topic"+topicName);
		return topicPublisher.publishMessage(message, msgType);

	}
}
