package com.jfinal.plugin.jms;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class TopicPublisher {
	private static final Logger logger = LoggerFactory.getLogger(TopicPublisher.class);

	protected String serverUrl;
	protected String username;
	protected String password;
	protected String topicName;
	protected int reConnectTimes;
	protected int reConnectInterval;

	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;
	private MessageProducer producer = null;

	public TopicPublisher(String serverUrl, String username, String password, String topicName) {
		this.serverUrl = serverUrl;
		this.username = username;
		this.password = password;
		this.topicName = topicName;
		initConnection();
	}

	public TopicPublisher(String serverUrl, String username, String password, String topicName, int reConnectTimes,
			int reConnectInterval) {
		this(serverUrl, username, password, topicName);
		this.reConnectTimes = reConnectTimes;
		this.reConnectInterval = reConnectInterval;
	}

	private void initConnection() {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(serverUrl);
		try {
			connection = factory.createConnection(username, password);
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createTopic(topicName);
			producer = session.createProducer(destination);
			producer.setTimeToLive(10L);
		} catch (JMSException e) {
			logger.error("init publisher error");
		}
	}

	public boolean publishMessage(Serializable object, int msg_type) {
		try {
			if (session == null) {
				if (!reConnect()) {
					logger.error("cant connected to JMS server");
					return false;
				}
			}
			logger.debug("publish message, msg_type:" + msg_type);
			ObjectMessage om = session.createObjectMessage(object);
			om.setIntProperty(JMSConstants.JMS_MESSAGE_TYPE, msg_type);
			producer.send(om);
		} catch (JMSException e) {
			logger.error("publish message error", e);
			return false;
		}
		return true;
	}

	private boolean reConnect() {
		int times = reConnectTimes;
		while (times-- > 0) {
			logger.debug("reConnectTimes" + times);
			initConnection();
			if (session != null) {
				return true;
			}
			try {
				Thread.sleep(reConnectInterval * 60 * 1000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return false;
	}

	public void closeConnection() {
		if (producer != null) {
			try {
				producer.close();
			} catch (JMSException e) {
				logger.error("close producer error", e);
			}
		}
		if (session != null) {
			try {
				session.close();
			} catch (JMSException e) {
				logger.error("close session error", e);
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (JMSException e) {
				logger.error("close connection error", e);
			}
		}
	}
}
