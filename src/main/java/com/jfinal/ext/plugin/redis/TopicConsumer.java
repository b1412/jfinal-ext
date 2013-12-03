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
import java.util.Set;
import java.util.concurrent.TimeUnit;

import redis.clients.jedis.Tuple;

import com.jfinal.log.Logger;

public class TopicConsumer {
    protected final Logger logger = Logger.getLogger(getClass());

    private TopicNest topic;
    private TopicNest subscriber;
    private String id;
    private int interval = 1000;

    public TopicConsumer(final String id, final String topic) {
        this.topic = new TopicNest("topic:" + topic);
        this.subscriber = new TopicNest(this.topic.cat("subscribers").key());
        this.id = id;
    }

    public TopicConsumer interval(int interval) {
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void consume(JedisMessage callback) {
        while (true) {
            Serializable  message = readUntilEnd();
            if (message != null) {
                callback.onMessage(message);
                goNext();
            } else {
                waitForMessages();
            }
        }
    }

    public  <T extends Serializable> T consume() {
        T message = readUntilEnd();
        goNext();
        return message;
    }

    private <T extends Serializable> T readUntilEnd() {
        if (unreadMessages() > 0) {
            Serializable message = read();
            return (T)message;
        }
        return null;
    }

    private void goNext() {
        JedisKit.zincrby(subscriber.key(), 1, id);
    }

    private int getLastReadMessage() {
        Double lastMessageRead = JedisKit.zscore(subscriber.key(), id);
        if (lastMessageRead == null) {
            Set<Tuple> zrangeWithScores = JedisKit.zrangeWithScores(subscriber.key(), 0, 1);
            if (zrangeWithScores.iterator().hasNext()) {
                Tuple next = zrangeWithScores.iterator().next();
                Integer lowest = (int) next.getScore() - 1;
                JedisKit.zadd(subscriber.key(), lowest, id);
                return lowest;
            } else {
                return 0;
            }
        }
        return lastMessageRead.intValue();
    }

    private int getTopicSize() {
        String stopicSize = JedisKit.get(topic.key());
        int topicSize = 0;
        if (stopicSize != null) {
            topicSize = Integer.valueOf(stopicSize);
        }
        return topicSize;
    }

    public <T extends Serializable> T  read() {
        int lastReadMessage = getLastReadMessage();
        logger.debug("lastReadMessage "+lastReadMessage);
        String key = topic.cat("message").cat(lastReadMessage + 1).key();
        T  message = JedisKit.get(key);
        logger.info("consume the message," + "key[" + key + "],value[" + message + "]");
        return message;
    }

    public int unreadMessages() {
        return getTopicSize() - getLastReadMessage();
    }
}
