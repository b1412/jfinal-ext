package com.jfinal.ext.plugin.redis;

import redis.clients.jedis.Transaction;

public interface JedisAtom {

    void action(Transaction transaction);
}
