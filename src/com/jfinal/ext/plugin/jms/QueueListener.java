package com.jfinal.ext.plugin.jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.jfinal.log.Logger;

public class QueueListener implements MessageListener {

    protected final Logger logger = Logger.getLogger(getClass());

    protected String serverUrl;
    protected String username;
    protected String password;
    protected String queueName;

    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageConsumer consumer;
    private IMessageHandler messageHandler;

    public QueueListener(String serverUrl, String username, String password, String queueName,
            IMessageHandler messageHandler) {
        this.serverUrl = serverUrl;
        this.username = username;
        this.password = password;
        this.queueName = queueName;
        this.messageHandler = messageHandler;
        initListener();
    }

    private void initListener() {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(serverUrl);
            connection = connectionFactory.createConnection(username, password);
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(queueName);
            consumer = session.createConsumer(destination);
            consumer.setMessageListener(this);
        } catch (JMSException e) {
            logger.error("init error",e);
        }
    }

    @Override
    public void onMessage(Message message) {
        if (messageHandler != null) {
            messageHandler.handleMessage(message);
        } else {
            logger.warn("no message handler!! use default message handler");
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
