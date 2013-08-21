package com.jfinal.ext.plugin.jms;

import javax.jms.Message;
import javax.jms.ObjectMessage;

import com.jfinal.log.Logger;

public class DefaultMessageHandler implements IMessageHandler {
    
    protected final Logger logger = Logger.getLogger(getClass());
    @Override
    public void handleMessage(Message message) {
        if (message instanceof ObjectMessage) {
            logger.info("receive object message...");
        } else {
            logger.info("receive message...");
        }
    }
}
