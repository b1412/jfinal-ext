package com.jfinal.plugin.jms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.jms.MessageListener;

import com.jfinal.plugin.IPlugin;
import com.jfinal.util.StringKit;

public class JmsPlugin implements IPlugin {
	private String resoruceLocation = "jms.properties";
	
	private String serverUrl;
	private String username;
	private String password;
	
	private JmsSender jmsSender;
	private List<MessageListener> listeners = new ArrayList<MessageListener>();
	@Override
	public boolean start() {
		JmsConfig.init(resoruceLocation);
		initServerConfig();
		initSender();
		initReceiver();
		iniJmsKit();
		return true;
	}

	private void iniJmsKit() {
		JmsKit.init(jmsSender);
	}

	private void initServerConfig() {
		serverUrl =JmsConfig.getVal("serverUrl");
		username =JmsConfig.getVal("username");
		password =JmsConfig.getVal("password");
		System.out.println("serverUrl : "+serverUrl+" ,username : "+username+" ,password : "+password);
	}

	private void initReceiver() {
		String receiveQueues =JmsConfig.getVal("receiveQueues");
		System.out.println("receiveQueues :"+receiveQueues);
		if (StringKit.notBlank(receiveQueues)) {
			for (String queueName : receiveQueues.split(",")) {
				JmsReceive queueReceive = new JmsReceive( new ReceiveResolverFactory(resoruceLocation, "queue."+queueName));
				listeners.add(new QueueListener(serverUrl,username,password,queueName, queueReceive));
			}
		}
		String receiveTopics =JmsConfig.getVal("receiveTopics");
		System.out.println("receiveTopic :"+receiveTopics);
		if (StringKit.notBlank(receiveTopics)) {
			for (String topicName : receiveTopics.split(",")) {
				JmsReceive queueReceive = new JmsReceive( new ReceiveResolverFactory(resoruceLocation, "topic."+topicName));
				listeners.add(new TopicListener(serverUrl,username,password,topicName, queueReceive));
			}
		}
		System.out.println(listeners);
	}

	private void initSender() {
		jmsSender = new JmsSender();
		jmsSender.queueProducers = new HashMap<String, QueueProducer>();
		String sendQueues =JmsConfig.getVal("sendQueues");
		System.out.println("sendQueues :"+sendQueues);
		if (StringKit.notBlank(sendQueues)) {
			for (String queueName : sendQueues.split(",")) {
				jmsSender.queueProducers.put(queueName, new QueueProducer(serverUrl, username, password, queueName));
			}
		}
		String sendTopics =JmsConfig.getVal("sendTopics");
		
		System.out.println("sendTopics :"+sendTopics);
		if (StringKit.notBlank(sendTopics)) {
			jmsSender.topicPublishers = new HashMap<String, TopicPublisher>();
			for (String topicName : sendTopics.split(",")) {
				jmsSender.topicPublishers.put(topicName, new TopicPublisher(serverUrl, username, password, topicName));
			}
		}
	}

	@Override
	public boolean stop() {
		listeners.clear();
		return true;
	}

}
