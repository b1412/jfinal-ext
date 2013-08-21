package com.jfinal.ext.plugin.jms;

import javax.jms.Message;
import javax.jms.ObjectMessage;

import com.jfinal.log.Logger;

public class JmsReceive implements IMessageHandler {
    
    protected final Logger logger = Logger.getLogger(getClass());
    
    private ReceiveResolverFactory receiveResolverFactory;

    public JmsReceive(ReceiveResolverFactory receiveResolverFactory) {
        this.receiveResolverFactory = receiveResolverFactory;
    }

    @Override
    public void handleMessage(Message message) {
        try {
            int messageType = message.getIntProperty(JMSConstants.JMS_MESSAGE_TYPE);
            ObjectMessage objMsg = (ObjectMessage) message;
            logger.debug("msgType " + messageType + " objMsg :" + objMsg);
            ReceiveResolver resolver = this.receiveResolverFactory.createReceiveResolver(new Integer(messageType));
            if (resolver == null) {
                logger.error("cant find  ReceiveResolver with messageType = " + messageType);
                return;
            }
            resolver.resolve(objMsg.getObject());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }
}
