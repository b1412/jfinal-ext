package com.jfinal.ext.plugin.redis;

import com.jfinal.plugin.ehcache.IDataLoader;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * User: Michael
 * Email: yidongnan@gmail.com
 * Date: 13-10-11
 * Time: 上午7:21
 */
public class RedisKit {
	private static final Logger logger = Logger.getLogger(RedisKit.class);
	private static volatile RedisManager redisManager;

	static void init(RedisManager redisManager) {
		RedisKit.redisManager = redisManager;
	}

    public static RedisManager getRedisManager() {
        return redisManager;
    }

    public static void put(String cacheName, Object key, Object value) {
        RedisManager.getJedisCache(cacheName, redisManager).put(key,value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String cacheName, Object key) {
        Object obj = RedisManager.getJedisCache(cacheName, redisManager).get(key);
        return obj == null ? null : (T)obj;
    }

    public static List getKeys(String cacheName) {
        return RedisManager.getJedisCache(cacheName, redisManager).getKeys();
    }

    public static void remove(String cacheName, Object key) {
        RedisManager.getJedisCache(cacheName, redisManager).remove(key);
    }

    public static void removeAll(String cacheName) {
        RedisManager.getJedisCache(cacheName, redisManager).removeAll();
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String cacheName, Object key, IDataLoader dataLoader) {
        Object data = get(cacheName, key);
        if (data == null) {
            data = dataLoader.load();
            put(cacheName, key, data);
        }
        return (T)data;
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String cacheName, Object key, Class<? extends IDataLoader> dataLoaderClass) {
        Object data = get(cacheName, key);
        if (data == null) {
            try {
                IDataLoader dataLoader = dataLoaderClass.newInstance();
                data = dataLoader.load();
                put(cacheName, key, data);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return (T)data;
    }
}
