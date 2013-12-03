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

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.pool.impl.GenericObjectPool;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

import com.jfinal.ext.kit.ResourceKit;
import com.jfinal.kit.StringKit;
import com.jfinal.plugin.IPlugin;

public class JedisPlugin implements IPlugin {

    public JedisPool pool;
    private String config = "RedisConnector.properties";

    private String host = "localhost";
    private int port = 6379;
    private int timeout = 2000;
    private String password;
    private int maxactive = GenericObjectPool.DEFAULT_MAX_ACTIVE;
    private int maxidle = GenericObjectPool.DEFAULT_MAX_ACTIVE;
    private long maxwait = GenericObjectPool.DEFAULT_MAX_WAIT;
    private long minevictableidletimemillis = GenericObjectPool.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;
    private int minidle = GenericObjectPool.DEFAULT_MIN_IDLE;
    private int numtestsperevictionrun = GenericObjectPool.DEFAULT_NUM_TESTS_PER_EVICTION_RUN;
    private long softminevictableidletimemillis = GenericObjectPool.DEFAULT_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS;
    private long timebetweenevictionrunsmillis = GenericObjectPool.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;
    private byte whenexhaustedaction = GenericObjectPool.DEFAULT_WHEN_EXHAUSTED_ACTION;
    private boolean testwhileidle = GenericObjectPool.DEFAULT_TEST_WHILE_IDLE;
    private boolean testonreturn = GenericObjectPool.DEFAULT_TEST_ON_RETURN;
    private boolean testonborrow = GenericObjectPool.DEFAULT_TEST_ON_BORROW;

    public JedisPlugin() {
    }

    public JedisPlugin(String host) {
        this.host = host;
    }

    public JedisPlugin(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public JedisPlugin(String host, int port, int timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    public JedisPlugin config(String config) {
        this.config = config;
        return this;
    }

    @Override
    public boolean start() {
        Map<String, String> map = ResourceKit.readProperties(config);
        Set<Entry<String, String>> entrySet = map.entrySet();
        for (Entry<String, String> entry : entrySet) {
            parseSetting(entry.getKey(), entry.getValue().trim());
        }
        JedisShardInfo shardInfo = new JedisShardInfo(host, port, timeout);
        if (StringKit.notBlank(password)) {
            shardInfo.setPassword(password);
        }
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        setPoolConfig(poolConfig);
        pool = new JedisPool(poolConfig, shardInfo.getHost(), shardInfo.getPort(), shardInfo.getTimeout(),
                shardInfo.getPassword());
        JedisKit.init(pool);
        return true;
    }

    private void setPoolConfig(JedisPoolConfig poolConfig) {
        poolConfig.setMaxActive(maxactive);
        poolConfig.setMaxIdle(maxidle);
        poolConfig.setMaxWait(maxwait);
        poolConfig.setMinEvictableIdleTimeMillis(minevictableidletimemillis);
        poolConfig.setMinIdle(minidle);
        poolConfig.setNumTestsPerEvictionRun(numtestsperevictionrun);
        poolConfig.setSoftMinEvictableIdleTimeMillis(softminevictableidletimemillis);
        poolConfig.setTimeBetweenEvictionRunsMillis(timebetweenevictionrunsmillis);
        poolConfig.setWhenExhaustedAction(whenexhaustedaction);
        poolConfig.setTestWhileIdle(testwhileidle);
        poolConfig.setTestOnReturn(testonreturn);
        poolConfig.setTestOnBorrow(testonborrow);
    }

    @Override
    public boolean stop() {
        try {
            pool.destroy();
        } catch (Exception ex) {
            System.err.println("Cannot properly close Jedis pool:" + ex);
        }
        pool = null;
        return true;
    }

    private void parseSetting(String key, String value) {
        if ("timeout".equalsIgnoreCase(key)) {
            timeout = Integer.valueOf(value);
        } else if ("password".equalsIgnoreCase(key)) {
            password = value;
        } else if ("host".equalsIgnoreCase(key)) {
            host = value;
        } else if ("maxactive".equalsIgnoreCase(key)) {
            maxactive = Integer.valueOf(value);
        } else if ("maxidle".equalsIgnoreCase(key)) {
            maxidle = Integer.valueOf(value);
        } else if ("maxwait".equalsIgnoreCase(key)) {
            maxwait = Integer.valueOf(value);
        } else if ("minevictableidletimemillis".equalsIgnoreCase(key)) {
            minevictableidletimemillis = Long.valueOf(value);
        } else if ("minidle".equalsIgnoreCase(key)) {
            minidle = Integer.valueOf(value);
        } else if ("numtestsperevictionrun".equalsIgnoreCase(key)) {
            numtestsperevictionrun = Integer.valueOf(value);
        } else if ("softminevictableidletimemillis".equalsIgnoreCase(key)) {
            softminevictableidletimemillis = Long.valueOf(value);
        } else if ("timebetweenevictionrunsmillis".equalsIgnoreCase(key)) {
            timebetweenevictionrunsmillis = Long.valueOf(value);
        } else if ("whenexhaustedaction".equalsIgnoreCase(key)) {
            if ("WHEN_EXHAUSTED_BLOCK".equalsIgnoreCase(value)) {
                whenexhaustedaction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
            } else if ("WHEN_EXHAUSTED_FAIL".equalsIgnoreCase(value)) {
                whenexhaustedaction = GenericObjectPool.WHEN_EXHAUSTED_FAIL;
            } else if ("WHEN_EXHAUSTED_GROW".equalsIgnoreCase(value)) {
                whenexhaustedaction = GenericObjectPool.WHEN_EXHAUSTED_GROW;
            }
        } else if ("testwhileidle".equalsIgnoreCase(key)) {
            testwhileidle = Boolean.getBoolean(value);
        } else if ("testonreturn".equalsIgnoreCase(key)) {
            testonreturn = Boolean.getBoolean(value);
        } else if ("testonborrow".equalsIgnoreCase(key)) {
            testonborrow = Boolean.getBoolean(value);
        }
    }
}
