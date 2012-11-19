package com.jfinal.ext.plugin.config;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import com.jfinal.ext.route.AutoControllerRegist;
import com.jfinal.log.Logger;

public class ConfigKit {
	protected static Logger logger = Logger.getLogger(AutoControllerRegist.class);

	private static Map<String, String> map;

	private static Map<String, String> testMap;

	private static String classpath ;
	
	/**
	 * 
	 * @param includeResources
	 * @param excludeResources
	 */
	 static void init(List<String> includeResources,List<String> excludeResources) {
		classpath = ConfigKit.class.getClassLoader().getResource("").getFile();
		logger.debug("classpath: "+classpath);
		map = new HashMap<String, String>();
		testMap = new HashMap<String, String>();
		for (final String resource : includeResources) {
			logger.debug("include :" + resource);
			File[] propertiesFiles = null;
			propertiesFiles = new File(classpath).listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return Pattern.compile(resource).matcher(pathname.getName()).matches();
				}
			});
			for (File file : propertiesFiles) {
				String fileName = file.getAbsolutePath();
				logger.debug("fileName:" + fileName);
				if (fileName.endsWith("-test.properties"))
					continue;
				boolean excluded = false;
				for (final String exclude : excludeResources) {
					 if (Pattern.compile(exclude).matcher(file.getName()).matches()) {
						 excluded = true;
					 }
				}
				if (excluded) {
					continue;
				}
				Properties prop = new Properties();
				InputStream is;
				try {
					is = new FileInputStream(fileName);
					prop.load(is);
				} catch (FileNotFoundException e) {
				} catch (IOException e) {
				}
				Set<Object> keys = prop.keySet();
				for (Object key : keys) {
					map.put(key + "", prop.getProperty(key + "", ""));
				}
				String testFileName = fileName.substring(0,fileName.indexOf(".properties"))+ "-test.properties";
				Properties tprop = new Properties();
				try {
					InputStream tis = new FileInputStream(testFileName);
					tprop.load(tis);
				} catch (FileNotFoundException e) {
				} catch (IOException e) {
				}
				Set<Object> tkeys = prop.keySet();
				for (Object tkey : tkeys) {
					testMap.put(tkey + "", tprop.getProperty(tkey + "", ""));
				}
			}
		}
		logger.debug("map" + map);
		logger.debug("testMap" + testMap);
		logger.debug("init success!");
	}

	 public static String getStr(String key,String defaultVal) {
		 if (testMap == null || map == null) {
			 throw new RuntimeException(" the ConfigPlugin dident start");
		 }
		 Object val = testMap.get(key);
		 if ("".equals(val)) {
			 val = map.get(key);
		 }
		 return val == null ? defaultVal: val + "";
		 
	}
	public static String getStr(String key) {
		return getStr(key, "");
	}

	public static long getLong(String key) {
		return getLong(key, 0);
	}
	public static long getLong(String key,long defaultVal) {
		String val = getStr(key);
		if ("".equals(val)) {
			return defaultVal;
		}
		return Long.parseLong(val);
	}

	public static int getInt(String key,int defaultVal) {
		String val = getStr(key);
		if ("".equals(val)) {
			return defaultVal;
		}
		return Integer.parseInt(val);
	}
	public static int getInt(String key) {
		return getInt(key, 0);
	}
}
