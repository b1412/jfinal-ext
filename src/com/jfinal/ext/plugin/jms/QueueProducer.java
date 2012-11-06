package com.jfinal.ext.plugin.jms;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.jfinal.log.Logger;

public class QueueProducer {
	
	protected final Logger logger = Logger.getLogger(getClass());
	
	protected String serverUrl;
	protected String username;
	protected String password;
	protected String queueName;
	protected int reConnectTimes;
	protected int reConnectInterval;

	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;
	private MessageProducer producer = null;

	public QueueProducer(String serverUrl, String username, String password, String queueName) {
		this.serverUrl = serverUrl;
		this.username = username;
		this.password = password;
		this.queueName = queueName;
		initConnection();
	}

	public QueueProducer(String serverUrl, String username, String password, String queueName, int reConnectTimes,
			int reConnectInterval) {
		this(serverUrl, username, password, queueName);
		this.reConnectTimes = reConnectTimes;
		this.reConnectInterval = reConnectInterval;
	}

	private void initConnection() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(serverUrl);
		try {
			connection = connectionFactory.createConnection(username, password);
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(queueName);
			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			connection.start();
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

	public void closeConnection() {
		if (producer != null) {
			try {
				producer.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		if (session != null) {
			try {
				session.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean sendMessage(Serializable object, int msg_type) {
		try {
			if (session == null) {
				if (!reConnect()) {
					return false;
				}
			}
			logger.debug("send message, msg_type:" + msg_type);
			ObjectMessage om = session.createObjectMessage(object);
			om.setIntProperty(JMSConstants.JMS_MESSAGE_TYPE, msg_type);
			producer.send(destination, om);
		} catch (JMSException e) {
			e.printStackTrace();
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
				e.printStackTrace();
			}
		}
		return false;
	}
}
