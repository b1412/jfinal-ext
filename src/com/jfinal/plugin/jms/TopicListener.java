package com.jfinal.plugin.jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopicListener implements MessageListener {
    private static Logger logger = LoggerFactory.getLogger(TopicListener.class);

    protected String serverUrl;
    protected String username;
    protected String password;
    protected String topicName;

    private Connection connection = null;
    private Session session = null;
    private Destination destination = null;
    private MessageConsumer consumer = null;
    private IMessageHandler messageHandler = null;

    public TopicListener(String serverUrl, String username, String password, String topicName, IMessageHandler messageHandler) {
        this.serverUrl = serverUrl;
        this.username = username;
        this.password = password;
        this.topicName = topicName;
        this.messageHandler = messageHandler;
        initListener();
    }

    private void initListener() {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(serverUrl);
            connection = connectionFactory.createConnection(username, password);
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createTopic(topicName);
            consumer = session.createConsumer(destination);
            consumer.setMessageListener(this);
        } catch (JMSException e) {
            logger.error("init listener error", e);
        }
    }

    @Override
	public void onMessage(Message message) {
        if (messageHandler != null) {
            messageHandler.handleMessage(message);
        } else {
            logger.error("no message handler!! use default message handler");
            new DefaultMessageHandler().handleMessage(message);
        }
    }

    public void closeConnection() {
        if (consumer != null) {
            try {
                consumer.close();
            } catch (JMSException e) {
                logger.error("close consumer error", e);
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
