package com.michael.blog.plugin.redis;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * User: Michael
 * Email: yidongnan@gmail.com
 * Date: 13-10-11
 * Time: 上午7:21
 */
public class RedisManager {
	private static JedisPool pool;
	private String host;
	private int port;
	private int dbIndex;
    private String password;
    private static final Logger logger = Logger.getLogger(RedisManager.class);

    public static RedisCache getJedisCache(String cacheName, RedisManager redisManager) {
        Jedis jedis = null;
        RedisCache cache = null;
        try {
            jedis = RedisManager.getJedisPool().getResource();
             cache = new RedisCache(cacheName, jedis);
            logger.debug("get redis from pool success," + jedis);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("get redis from pool fail", e);
        }
        if (jedis == null) {
            try {
                jedis = new Jedis(redisManager.getHost(), redisManager.getPort(), 2000);
                jedis.select(redisManager.getDbIndex());
                cache = new RedisCache(cacheName, jedis);
                logger.debug("create redis" + redisManager.getDbIndex() + "success," + jedis);
            } catch (Exception e) {
                logger.error("create redis"+redisManager.getDbIndex()+"fail", e);
            }
        }
        return cache;
    }


    public static void returnResource(Jedis jedis, RedisManager redisManager) {
        if (redisManager == null || RedisManager.getJedisPool() == null || jedis == null) {
            return;
        }
        RedisManager.getJedisPool().returnResource(jedis);
        logger.debug("free redis," + jedis);
    }


    public RedisManager(String host, int port, int dbIndex) {
		this.host = host;
		this.port = port;
		this.dbIndex = dbIndex;
	}

	public void init() {
		JedisPoolConfig config = new JedisPoolConfig();

		config.setMaxActive(20);
		config.setMaxIdle(20);
		config.setMaxWait(1000);

		pool = new JedisPool(config, host, port, 2000, null, dbIndex);
	}

	public static JedisPool getJedisPool() {
		return pool;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public int getDbIndex() {
		return dbIndex;
	}

	public void destroy() {
		pool.destroy();
	}
}
