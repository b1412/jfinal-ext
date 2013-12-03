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
package com.jfinal.ext.plugin.redis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.jms.IllegalStateException;

import com.google.common.collect.Maps;
import com.jfinal.log.Logger;

public class QueueConsumer {
    final Logger LOG = Logger.getLogger(getClass());

    static final String SEPARATER = "-";
    static final String PROCESSINGLIST = SEPARATER + "processing";
    static final String PREFIX = "queue" + SEPARATER;

    static Map<String, QueueConsumer> existingConsumers = Maps.newHashMap();

    int interval = 1000;

    boolean start = false;

    String queueName;

    private QueueConsumer(String queueName) {
        this.queueName = queueName;
    }

    public static QueueConsumer create(String queueName) {
        QueueConsumer consumer = existingConsumers.get(queueName);
        if (consumer == null) {
            consumer = new QueueConsumer(queueName);
        } else {
            throw new IllegalArgumentException("The consumer named " + queueName + " already exists");
        }
        return consumer;
    }

    public QueueConsumer interval(int interval) {
        this.interval = interval;
        return this;
    }

    private void waitForMessages() {
        try {
            TimeUnit.MILLISECONDS.sleep(interval);
        } catch (InterruptedException e) {
            // TODO
            e.printStackTrace();
        }
    }

    public <T extends Serializable> T consume() {
        return JedisKit.rpoplpush(queueName(), processingListName());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void consume(final JedisMessage callback) {
        if (start) {
            throw new RuntimeException("The Consumer named " + queueName + " is working");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    JedisKit.rpoplpush(queueName(), processingListName());
                    List<Serializable> message = JedisKit.lrange(processingListName(), -1, -1);
                    if (message.isEmpty()) {
                        waitForMessages();
                    } else {
                        callback.onMessage(message.get(0));
                        JedisKit.rpop(processingListName());
                    }
                }

            }
        }).start();
    }

    private String processingListName() {
        return PREFIX + queueName + PROCESSINGLIST;
    }

    private String queueName() {
        return PREFIX + queueName;
    }

}
