package com.jfinal.plugin.jms;

import javax.jms.Message;

/**
 * Created by IntelliJ IDEA.
 * User: madoldman
 * Date: 11-12-21
 * Time: 下午4:40
 * To change this template use File | Settings | File Templates.
 */
public interface IMessageHandler {
    public void handleMessage(Message message);
}
