/**
 * Copyright (c) 2011-2013, kidzhou 周磊 (zhouleib1412@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jfinal.ext.plugin.jms;

import java.util.HashMap;
import java.util.List;

import javax.jms.MessageListener;

import com.google.common.collect.Lists;
import com.jfinal.ext.plugin.config.ConfigPlugin;
import com.jfinal.kit.StringKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;

public class JmsPlugin implements IPlugin {

    protected final Logger logger = Logger.getLogger(getClass());

    private String resoruceLocation = "jms.properties";

    private ConfigPlugin configPlugin;
    private String serverUrl;
    private String username;
    private String password;

    private JmsSender jmsSender;
    private List<MessageListener> listeners = Lists.newArrayList();

    public JmsPlugin() {
    }

    public JmsPlugin(ConfigPlugin configPlugin) {
        this.configPlugin = configPlugin;
    }

    public JmsPlugin(String resoruceLocation) {
        this.resoruceLocation = resoruceLocation;
    }

    public JmsPlugin(String resoruceLocation, ConfigPlugin configPlugin) {
        this.resoruceLocation = resoruceLocation;
        this.configPlugin = configPlugin;
    }

    @Override
    public boolean start() {
        JmsConfig.init(resoruceLocation, configPlugin);
        initServerConfig();
        initSender();
        initReceiver();
        iniJmsKit();
        return true;
    }

    private void iniJmsKit() {
        JmsKit.init(jmsSender);
    }

    private void initServerConfig() {
        serverUrl = JmsConfig.getStr("serverUrl");
        username = JmsConfig.getStr("username");
        password = JmsConfig.getStr("password");
        logger.debug("serverUrl : " + serverUrl + " ,username : " + username + " ,password : " + password);
    }

    private void initReceiver() {
        String receiveQueues = JmsConfig.getStr("receiveQueues");
        logger.debug("receiveQueues :" + receiveQueues);
        if (StringKit.notBlank(receiveQueues)) {
            for (String queueName : receiveQueues.split(",")) {
                JmsReceive queueReceive = new JmsReceive(new ReceiveResolverFactory(resoruceLocation, "queue."
                        + queueName));
                listeners.add(new QueueListener(serverUrl, username, password, queueName, queueReceive));
            }
        }
        String receiveTopics = JmsConfig.getStr("receiveTopics");
        logger.debug("receiveTopic :" + receiveTopics);
        if (StringKit.notBlank(receiveTopics)) {
            for (String topicName : receiveTopics.split(",")) {
                JmsReceive queueReceive = new JmsReceive(new ReceiveResolverFactory(resoruceLocation, "topic."
                        + topicName));
                listeners.add(new TopicListener(serverUrl, username, password, topicName, queueReceive));
            }
        }
        logger.debug(listeners.toString());
    }

    private void initSender() {
        jmsSender = new JmsSender();
        jmsSender.queueProducers = new HashMap<String, QueueProducer>();
        String sendQueues = JmsConfig.getStr("sendQueues");
        logger.debug("sendQueues :" + sendQueues);
        if (StringKit.notBlank(sendQueues)) {
            for (String queueName : sendQueues.split(",")) {
                jmsSender.queueProducers.put(queueName, new QueueProducer(serverUrl, username, password, queueName));
            }
        }
        String sendTopics = JmsConfig.getStr("sendTopics");

        logger.debug("sendTopics :" + sendTopics);
        if (StringKit.notBlank(sendTopics)) {
            jmsSender.topicPublishers = new HashMap<String, TopicPublisher>();
            for (String topicName : sendTopics.split(",")) {
                jmsSender.topicPublishers.put(topicName, new TopicPublisher(serverUrl, username, password, topicName));
            }
        }
    }

    @Override
    public boolean stop() {
        listeners.clear();
        return true;
    }

}
