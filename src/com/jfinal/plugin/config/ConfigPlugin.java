package com.jfinal.plugin.config;

import com.jfinal.plugin.IPlugin;

public class ConfigPlugin implements IPlugin {
	private String floder;
	public ConfigPlugin(String resources){
		this.floder = floder;
	}
	@Override
	public boolean start() {
		ConfigKit.init(floder);
		return true;
	}

	@Override
	public boolean stop() {
		return true;
	}

}
