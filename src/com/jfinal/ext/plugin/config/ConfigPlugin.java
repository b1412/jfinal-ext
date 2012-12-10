package com.jfinal.ext.plugin.config;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;

public class ConfigPlugin implements IPlugin {
	
	protected  Logger logger = Logger.getLogger(getClass());
	
	private static String suffix = "txt";
	
	private final  List<String> includeResources = new ArrayList<String>();
	
	private final List<String> excludeResources = new ArrayList<String>();
	
	private boolean reload = true;
	
	public ConfigPlugin() {}
	
	public ConfigPlugin(String includeResource) {
		this.includeResources.add(includeResource);
	}
	public ConfigPlugin(String includeResource,String excludeResource) {
		this.includeResources.add(includeResource);
		this.excludeResources.add(excludeResource);
	}
	
	public boolean excludeResource(String resource){
		return excludeResources.add(resource);
	}
	
	public boolean addResource(String resource) {
		return includeResources.add(resource);
	}
	public static void setSuffix(String suffix){
		ConfigPlugin.suffix=suffix;
	}
	
	public static String getSuffix(){
		return ConfigPlugin.suffix;
	}
	public void setReload(boolean reload) {
		this.reload = reload;
	}
	@Override
	public boolean start() {
		ConfigKit.init(includeResources,excludeResources,reload);
		return true;
	}

	@Override
	public boolean stop() {
		return true;
	}


}
