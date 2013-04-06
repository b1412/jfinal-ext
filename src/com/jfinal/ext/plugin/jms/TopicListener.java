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

public class TopicListener implements MessageListener {

    protected final Logger logger = Logger.getLogger(getClass());

    protected String serverUrl;
    protected String username;
    protected String password;
    protected String topicName;

    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageConsumer consumer;
    private IMessageHandler messageHandler;

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
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message message) {
        if (messageHandler != null) {
            logger.error("MessageHandler is null!please set a messageHandler");
            messageHandler.handleMessage(message);
        } else {
            new DefaultMessageHandler().handleMessage(message);
        }
    }

    public void closeConnection() {
        if (consumer != null) {
            try {
                consumer.close();
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
}
