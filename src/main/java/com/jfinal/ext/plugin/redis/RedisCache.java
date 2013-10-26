package com.jfinal.ext.plugin.redis;

import org.apache.commons.lang3.SerializationUtils;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * User: Michael
 * Email: yidongnan@gmail.com
 * Date: 13-10-11
 * Time: 上午7:21
 */
public class RedisCache {
    private String cacheName;
    private Jedis cache;

    public Object get(Object key) {
        if (null == key)
            return null;
        byte[] b = cache.get((cacheName+":"+String.valueOf(key)).getBytes());
        RedisManager.getJedisPool().returnResource(cache);
        return b == null ? null : SerializationUtils.deserialize(b);
    }

    public void put(Object key, Object value) {
        cache.set((cacheName+":"+String.valueOf(key)).getBytes(),
                value == null ? null : SerializationUtils
                        .serialize((Serializable) value));
        RedisManager.getJedisPool().returnResource(cache);
    }

    public List getKeys(){
        List<Object> keys = new ArrayList<Object>();
        Set<byte[]> list = cache.keys(String.valueOf("*").getBytes());
        for (byte[] bs : list) {
            keys.add(bs == null ? null : SerializationUtils.deserialize(bs));
        }
        RedisManager.getJedisPool().returnResource(cache);
        return keys;
    }



    public void remove(Object key) {
        cache.expire((cacheName+":"+String.valueOf(key)).getBytes(), 0);
        RedisManager.getJedisPool().returnResource(cache);
    }

    public void removeAll(){
            List keys = this.getKeys();
            for (Object key : keys) {
                this.remove(key);
            }
    }

    public RedisCache(String region, Jedis cache) {
        this.cache = cache;
        this.cacheName = region;
    }

    public Jedis getCache() {
        return cache;
    }

    public void setCache(Jedis cache) {
        this.cache = cache;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }
}
