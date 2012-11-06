package com.jfinal.ext.plugin.config;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;

public class ConfigPlugin implements IPlugin {
	
	protected  Logger logger = Logger.getLogger(getClass());
	private final  List<String> resources = new ArrayList<String>();
	
	public ConfigPlugin() {
	}
	public ConfigPlugin(String resource) {
		this.resources.add(resource);
	}
	public void addResource(String resource) {
		this.resources.add(resource);
	}
	public void addResources(String resources){
		this.resources.add(resources);
	}
	@Override
	public boolean start() {
		ConfigKit.init(resources);
		return true;
	}

	@Override
	public boolean stop() {
		return true;
	}


}
