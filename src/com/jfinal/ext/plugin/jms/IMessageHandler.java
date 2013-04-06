package com.jfinal.ext.plugin.jms;

import javax.jms.Message;

public interface IMessageHandler {
    void handleMessage(Message message);
}
