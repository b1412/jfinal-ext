package test.com.jfinal.ext.plugin.config;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jfinal.ext.plugin.config.ConfigKit;
import com.jfinal.ext.plugin.config.ConfigPlugin;
import com.jfinal.log.Logger;

public class TestConfig {
	@BeforeClass
	public static void init() {
		Logger.init();
		ConfigPlugin configPlugin = new ConfigPlugin();
		configPlugin.addResource(".*.txt");
		ConfigPlugin.setSuffix("txt");
		configPlugin.start();
	}

	@Test
	public void testGetStr() throws InterruptedException {
		Assert.assertEquals("test",ConfigKit.getStr("name"));
		Assert.assertEquals(1,ConfigKit.getInt("age"));
	}

}
