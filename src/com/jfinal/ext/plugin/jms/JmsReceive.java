package com.jfinal.ext.plugin.jms;

import javax.jms.Message;
import javax.jms.ObjectMessage;

public class JmsReceive implements IMessageHandler {
	private ReceiveResolverFactory receiveResolverFactory;

	public JmsReceive(ReceiveResolverFactory receiveResolverFactory) {
		this.receiveResolverFactory = receiveResolverFactory;
	}

	@Override
	public void handleMessage(Message message) {
		try {
			int messageType = message.getIntProperty(JMSConstants.JMS_MESSAGE_TYPE);
			ObjectMessage objMsg = (ObjectMessage) message;
			System.out.println("msgType " + messageType+" objMsg :" + objMsg);
			ReceiveResolver resolver = this.receiveResolverFactory.createReceiveResolver(new Integer(messageType));
			if (resolver == null) {
				System.out.println("cant find  ReceiveResolver with messageType = "+ messageType);
				return;
			}
			resolver.resolve(objMsg.getObject());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}