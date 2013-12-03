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
import java.util.Set;

import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

import com.jfinal.ext.kit.SerializableKit;
import com.jfinal.log.Logger;

public class TopicPoducer {
    protected final Logger logger = Logger.getLogger(getClass());

    private TopicNest topic;
    private TopicNest subscriber;

    public TopicPoducer(String topic) {
        this.topic = new TopicNest("topic:" + topic);
        this.subscriber = new TopicNest(this.topic.cat("subscribers").key());
    }

    public void publish(Serializable message) {
        publish(message, 0);
    }

    protected Integer getNextMessageId() {
        String slastMessageId = JedisKit.get(topic.key());
        Integer lastMessageId = 0;
        if (slastMessageId != null) {
            lastMessageId = Integer.parseInt(slastMessageId);
        }
        lastMessageId++;
        logger.debug(topic.key() + " nextMessageId " + lastMessageId);
        return lastMessageId;
    }

    /** 删除最近消费的消息 */
    public void clean() {
        Set<Tuple> zrangeWithScores = JedisKit.zrangeWithScores(subscriber.key(), 0, 1);
        Tuple next = zrangeWithScores.iterator().next();
        Integer lowest = (int) next.getScore();
        String key = topic.cat("message").cat(lowest).key();
        logger.debug("clean key "+key);
        JedisKit.del(key);
    }
        /**
         *
         * @param message
         *            menssage
         * @param seconds
         *            expiry time
         */
    public void publish(final Serializable message, final int seconds) {
        List<Object> exec = null;
        do {
            JedisKit.watch(topic.key());
            exec = JedisKit.tx(new JedisAtom() {
                @Override
                public void action(Transaction trans) {
                    Integer nextMessageId = getNextMessageId();
                    String msgKey = topic.cat("message").cat(nextMessageId).key();
                    if(message instanceof  String){
                        trans.set(msgKey, (String)message);
                    }else{
                        trans.set(msgKey.getBytes(),SerializableKit.toByteArray(message));
                    }
                    logger.info("produce a message,key[" + msgKey + "],message[" + message+"]");
                    trans.set(topic.key(), nextMessageId.toString());
                    if (seconds > 0) {
                        trans.expire(msgKey, seconds);
                    }
                }
            });

        } while (exec == null);
    }
}
