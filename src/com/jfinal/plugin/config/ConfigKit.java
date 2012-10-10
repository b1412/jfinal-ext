package com.jfinal.plugin.config;

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

public class ConfigKit {
	private static Map<String, String> map;

	private static Map<String, String> testMap;

	private static String classpath ;
	
	
	/**
	 * the floders in classpath
	 * 
	 * @param resources
	 */
	static void init(List<String> resources) {
		classpath = ConfigKit.class.getClassLoader().getResource("").getFile();
		System.out.println("classpath: "+classpath);
		map = new HashMap<String, String>();
		testMap = new HashMap<String, String>();
		for (final String resource : resources) {
			System.out.println("floder :" + resource);
			File[] propertiesFiles = null;
			propertiesFiles = new File(classpath)
					.listFiles(new FileFilter() {

						@Override
						public boolean accept(File pathname) {
							System.out.println("fileName: "+pathname.getName());
							return Pattern.compile(resource).matcher(pathname.getName()).matches();
						}
					});
			System.out.println("propertiesFiles size :"
					+ propertiesFiles.length);
			for (File file : propertiesFiles) {
				String fileName = file.getAbsolutePath();
				System.out.println("fileName:" + fileName);
				if (fileName.endsWith("-test.properties"))
					continue;
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
					System.out.println("[" + key + "="
							+ prop.getProperty(key + "", "") + "]");
					map.put(key + "", prop.getProperty(key + "", ""));
				}

				String testFileName = fileName.substring(0,
						fileName.indexOf(".properties"))
						+ "-test.properties";
				System.out.println("testFileName : " + testFileName);

				Properties tprop = new Properties();
				try {
					InputStream tis = new FileInputStream(testFileName);
					tprop.load(tis);
				} catch (FileNotFoundException e) {
				} catch (IOException e) {
				}
				Set<Object> tkeys = prop.keySet();
				for (Object tkey : tkeys) {
					System.out.println("[" + tkey + "="
							+ tprop.getProperty(tkey + "", "") + "]");
					testMap.put(tkey + "", tprop.getProperty(tkey + "", ""));
				}

			}
		}
		System.out.println("map" + map);
		System.out.println("testMap" + testMap);
		System.out.println("init success!");
	}

	public static String getStr(String key) {
		if (testMap == null || map == null) {
			throw new RuntimeException(" the ConfigPlugin dident start");
		}
		Object val = testMap.get(key);
		if ("".equals(val)) {
			val = map.get(key);
		}
		return val == null ? "" : val + "";
	}

	public static long getLong(String key) {
		String val = getStr(key);
		if ("".equals(val)) {
			val = "0";
		}
		return Long.parseLong(val);
	}

	public static int getInt(String key) {
		String val = getStr(key);
		if ("".equals(val)) {
			val = "0";
		}
		return Integer.parseInt(val);
	}
}
