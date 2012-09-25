package com.jfinal.plugin.jms;

import javax.jms.Message;
import javax.jms.ObjectMessage;

public class DefaultMessageHandler implements IMessageHandler {

	public void handleMessage(Message message) {
		if (message instanceof ObjectMessage) {
			System.out.println("receive object message...");
		} else {
			System.out.println("receive message...");
		}
	}
}
