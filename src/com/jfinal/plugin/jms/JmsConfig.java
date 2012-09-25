package com.jfinal.plugin.jms;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class JmsConfig {
	private static  Properties properties;
	
	public synchronized static void init(String resoruceLocation) {
		if(properties!=null){
			return;
		}
		InputStream is = JmsPlugin.class.getClassLoader().getResourceAsStream(resoruceLocation);
		if (is == null) {
			throw new RuntimeException("cant find properties in location :"+ resoruceLocation);
		}
		properties = new Properties();
		try {
			properties.load(is);
		} catch (IOException e) {
			throw new RuntimeException("cant load properties in location :"+ resoruceLocation, e);
		}
	}
		
	public static String getVal(String key) {
		Object objVal = properties.get(key);
		return objVal==null?"":objVal+"";
	}
	
	public static Set<String> keys(){
		Set<String> keySet = new HashSet<String>();
		for (Object objVal : properties.keySet()) {
			keySet.add(objVal+"");
		}
		return keySet;
	}
	
}
