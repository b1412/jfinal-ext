package com.jfinal.ext.plugin.jms;

import java.io.Serializable;
import java.util.Map;

import com.jfinal.log.Logger;

public class JmsSender {
    protected final Logger logger = Logger.getLogger(getClass());

    Map<String, QueueProducer> queueProducers;
    Map<String, TopicPublisher> topicPublishers;

    public boolean queueSend(String queueName, Serializable message, int msgType) {
        if (queueProducers == null) {
            logger.error("JmsPlugin not start");
            return false;
        }
        QueueProducer queueProducer = queueProducers.get(queueName);
        logger.info("send msg " + message + "to queue " + queueName + " ,msgType " + msgType);
        return queueProducer.sendMessage(message, msgType);

    }

    public boolean topicSend(String topicName, Serializable message, int msgType) {
        if (topicPublishers == null) {
            logger.error("JmsPlugin not start");
            return false;
        }

        TopicPublisher topicPublisher = topicPublishers.get(topicName);
        logger.info("send msg " + message + "to topic" + topicName);
        return topicPublisher.publishMessage(message, msgType);

    }
}
