package com.michael.blog.plugin.redis;

import com.jfinal.plugin.IPlugin;

/**
 * User: Michael
 * Email: yidongnan@gmail.com
 * Date: 13-10-11
 * Time: 上午7:21
 */
public class RedisPlugin implements IPlugin {
	private static RedisManager redisManager;

	public RedisPlugin(String host, int port, int dbIndex) {
		RedisPlugin.redisManager = new RedisManager(host, port, dbIndex);
	}

	@Override
	public boolean start() {
		redisManager.init();
        RedisKit.init(redisManager);
		return true;
	}

	@Override
	public boolean stop() {
		redisManager.destroy();
		return true;
	}

}
