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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;
import redis.clients.util.SafeEncoder;

import com.google.common.collect.Lists;
import com.jfinal.ext.kit.SerializableKit;

public class JedisKit {
    private static Logger LOG = Logger.getLogger(JedisKit.class);
    private static JedisPool pool;

    public static void init(JedisPool pool) {
        JedisKit.pool = pool;
    }

    public static List<Object> tx(JedisAtom jedisAtom) {
        Jedis jedis = pool.getResource();
        Transaction trans = jedis.multi();
        jedisAtom.action(trans);
        return trans.exec();

    }

    public static <T> T call(JedisAction<T> jedisAction) {
        T result = null;
        Jedis jedis = pool.getResource();
        try {
            result = jedisAction.action(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage(), e);
        } finally {
            if (null != jedis)
                pool.returnResource(jedis);
        }
        return result;
    }

    /***
     * query one Object from Redis with key
     * 
     * @param key
     *            using it get value from key-value database
     * @return the Object which implements Serializable
     */

    public static <T extends Serializable> T get(final String key) {
        return call(new JedisAction<T>() {
            @SuppressWarnings("unchecked")
            @Override
            public T action(Jedis jedis) {
                Object result = null;
                byte[] retVal = jedis.get(SafeEncoder.encode(key));
                if (null != retVal) {
                    try {
                        result = SerializableKit.toObject(retVal);
                    } catch (Exception e) {
                        result = SafeEncoder.encode(retVal);
                    }
                }
                return (T) result;
            }
        });
    }

    /***
     * set object to key-value database with the specified key
     * 
     * @param key
     *            the unique key to indicate the value Object
     * @param value
     *            the value indicated by the key
     * @return return true while the set operation is succeed,false by failed
     */

    public static boolean set(final String key, final Serializable value) {
        return call(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                String retVal;
                if (value instanceof String) {
                    retVal = jedis.set(key, (String) value);
                } else {
                    retVal = jedis.set(SafeEncoder.encode(key), SerializableKit.toByteArray(value));
                }
                return "OK".equalsIgnoreCase(retVal);
            }
        });
    }

    /***
     * set object to key-value database with the specified key and EXPIRE time
     * 
     * @param key
     *            the unique key to indicate the value Object
     * @param value
     *            the value indicated by the key
     * @param seconds
     *            EXPIRE time
     * @return return true while the set operation is succeed,false by failed
     */

    public static boolean set(final String key, final Serializable value, final int seconds) {
        return call(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                byte[] bytes;
                if (value instanceof String) {
                    bytes = SafeEncoder.encode((String) value);
                } else {
                    bytes = SerializableKit.toByteArray(value);
                }
                String retVal = jedis.setex(SafeEncoder.encode(key), seconds, bytes);
                return "OK".equalsIgnoreCase(retVal);
            }
        });
    }

    /***
     * query multiple Object from Redis with key
     * 
     * @param keys
     *            using it get value from key-value database
     * @return the Object list which implements Serializable
     */

    public static List<Serializable> mquery(final String... keys) {

        return call(new JedisAction<List<Serializable>>() {
            @Override
            public List<Serializable> action(Jedis jedis) {
                List<Serializable> result = new ArrayList<Serializable>(keys.length);
                for (int index = 0; index < keys.length; index++)
                    result.add(null);
                byte[][] encodeKeys = new byte[keys.length][];
                for (int i = 0; i < keys.length; i++)
                    encodeKeys[i] = SafeEncoder.encode(keys[i]);
                List<byte[]> retVals = jedis.mget(encodeKeys);
                if (null != retVals) {
                    int index = 0;
                    for (byte[] val : retVals) {
                        if (null != val)
                            result.set(index, SerializableKit.toObject(val));
                        index++;
                    }
                }
                return result;
            }
        });
    }

    public static List<String> mqueryStr(final String... keys) {

        return call(new JedisAction<List<String>>() {
            @Override
            public List<String> action(Jedis jedis) {
                return jedis.mget(keys);
            }
        });
    }

    public static boolean msaveOrUpdate(final Map<String, Serializable> values) {
        return call(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                byte[][] encodeValues = new byte[values.size() * 2][];
                int index = 0;
                Iterator<Entry<String, Serializable>> iter = values.entrySet().iterator();
                while (iter.hasNext()) {
                    Entry<String, Serializable> entry = iter.next();
                    encodeValues[index++] = entry.getKey().getBytes();
                    encodeValues[index++] = SerializableKit.toByteArray(entry.getValue());
                }
                String retVal = jedis.mset(encodeValues);
                return "OK".equalsIgnoreCase(retVal);
            }
        });
    }

    public static boolean msaveOrUpdateStr(final Map<String, String> values) {

        return call(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                Iterator<Entry<String, String>> iter = values.entrySet().iterator();
                int index = 0;
                String[] encodeValues = new String[values.size() * 2];
                while (iter.hasNext()) {
                    Entry<String, String> entry = iter.next();
                    encodeValues[index++] = entry.getKey();
                    encodeValues[index++] = entry.getValue();
                }
                return "OK".equalsIgnoreCase(jedis.mset(encodeValues));
            }
        });
    }

    /**
     * query keys set by pattern
     */

    public static Set<String> keys(final String pattern) {
        return call(new JedisAction<Set<String>>() {
            @Override
            public Set<String> action(Jedis jedis) {
                return jedis.keys(pattern);
            }
        });

    }

    public static long del(final String... keys) {
        return call(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                byte[][] encodeKeys = new byte[keys.length][];
                for (int i = 0; i < keys.length; i++)
                    encodeKeys[i] = SafeEncoder.encode(keys[i]);
                return jedis.del(encodeKeys);
            }
        });
    }

    public static long listAdd(final String key, final Serializable value) {
        return call(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.rpush(SafeEncoder.encode(key), SerializableKit.toByteArray(value));
            }
        });
    }

    public static long listAddFirst(final String key, final Serializable value) {
        return call(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.lpush(SafeEncoder.encode(key), SerializableKit.toByteArray(value));
            }
        });
    }

    public static String type(final String key) {
        return call(new JedisAction<String>() {
            @Override
            public String action(Jedis jedis) {
                return jedis.type(SafeEncoder.encode(key));
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> queryList(final String key, final int start, final int end) {
        return call(new JedisAction<List<T>>() {
            @Override
            public List<T> action(Jedis jedis) {
                List<T> result = Lists.newArrayList();
                List<byte[]> retVals = jedis.lrange(SafeEncoder.encode(key), start, end);
                if (retVals != null) {
                    for (byte[] val : retVals) {
                        if (null != val)
                            result.add((T) SerializableKit.toObject(val));
                    }
                }
                return result;
            }
        });

    }

    public static long listSize(final String key) {
        return call(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.llen(SafeEncoder.encode(key));
            }
        });
    }

    public static boolean listTrim(final String key, final int start, final int end) {
        return call(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return "OK".equalsIgnoreCase(jedis.ltrim(SafeEncoder.encode(key), start, end));
            }
        });
    }

    public static long incrementAndGet(final String key) {
        return call(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.incr(key);
            }
        });
    }

    public static long decrementAndGet(final String key) {
        return call(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.decr(key);
            }
        });
    }

    public static long queryLong(final String key) {
        return call(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return Long.valueOf(jedis.get(key));
            }
        });
    }

    public static boolean hmset(final String key, final Map<String, String> values) {
        return call(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return "OK".equals(jedis.hmset(key, values));
            }
        });
    }

    public static List<String> hvals(final String key) {
        return call(new JedisAction<List<String>>() {
            @Override
            public List<String> action(Jedis jedis) {
                return jedis.hvals(key);
            }
        });
    }

    public static List<String> hmget(final String key, final String... fields) {
        return call(new JedisAction<List<String>>() {
            @Override
            public List<String> action(Jedis jedis) {
                return jedis.hmget(key, fields);
            }
        });
    }

    public static Double zincrby(final String key, final double score, final String member) {
        return call(new JedisAction<Double>() {
            @Override
            public Double action(Jedis jedis) {
                return jedis.zincrby(key, score, member);
            }
        });
    }

    public static Double zscore(final String key, final String score) {
        return call(new JedisAction<Double>() {
            @Override
            public Double action(Jedis jedis) {
                return jedis.zscore(key, score);
            }
        });
    }

    public static Long zadd(final String key, final double score, final String member) {
        return call(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.zadd(key, score, member);
            }
        });
    }

    public static Set<Tuple> zrangeWithScores(final String key, final long start, final long end) {
        return call(new JedisAction<Set<Tuple>>() {
            @Override
            public Set<Tuple> action(Jedis jedis) {
                return jedis.zrangeWithScores(key, start, end);
            }
        });
    }

    public static String watch(final String... keys) {
        return call(new JedisAction<String>() {
            @Override
            public String action(Jedis jedis) {
                return jedis.watch(keys);
            }
        });
    }

    public static Long lpush(final String key, final Serializable value) {
        return call(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                Long retVal;
                if (value instanceof String) {
                    retVal = jedis.lpush(key, (String) value);
                } else {
                    retVal = jedis.lpush(SafeEncoder.encode(key), SerializableKit.toByteArray(value));
                }
                return retVal;
            }
        });
    }
    public static <T extends Serializable> T rpop(final String key) {
        return call(new JedisAction<T>() {
            @SuppressWarnings("unchecked")
            @Override
            public T action(Jedis jedis) {
                Object result = null;
                byte[] retVal = jedis.rpop(SafeEncoder.encode(key));
                if (null != retVal) {
                    try {
                        result = SerializableKit.toObject(retVal);
                    } catch (Exception e) {
                        result = SafeEncoder.encode(retVal);
                    }
                }
                return (T) result;
            }
        });
    }

    public static <T extends Serializable> List<T> lrange(final String key, final long start, final long end) {
        return call(new JedisAction<List<T>>() {
            @SuppressWarnings("unchecked")
            @Override
            public List<T> action(Jedis jedis) {
                List<T> list = Lists.newArrayList();
                List<byte[]> results = jedis.lrange(SafeEncoder.encode(key), start, end);
                for (byte[] result : results) {
                    try {
                        list.add((T) SerializableKit.toObject(result));
                    } catch (Exception e) {
                        list.add((T) SafeEncoder.encode(result));
                    }
                }
                return list;
            }
        });
    }

    public static <T extends Serializable> T rpoplpush(final String srckey, final String dstkey) {
        return call(new JedisAction<T>() {
            @Override
            @SuppressWarnings("unchecked")
            public T action(Jedis jedis) {
                Object result = null;
                byte[] retVal = jedis.rpoplpush(SafeEncoder.encode(srckey), SafeEncoder.encode(dstkey));
                if (null != retVal) {
                    try {
                        result = SerializableKit.toObject(retVal);
                    } catch (Exception e) {
                        result = SafeEncoder.encode(retVal);
                    }
                }
                return (T) result;
            }
        });
    }

    public static Long lrem(String key, Serializable value) {
        return lrem(key, 1, value);
    }

    public static Long lrem(final String key, final long count, final Serializable value) {
        return call(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                Long retVal;
                if (value instanceof String) {
                    retVal = jedis.lrem(key, count, (String) value);
                } else {
                    retVal = jedis.lrem(SafeEncoder.encode(key), count, SerializableKit.toByteArray(value));
                }
                return retVal;
            }
        });
    }
}
