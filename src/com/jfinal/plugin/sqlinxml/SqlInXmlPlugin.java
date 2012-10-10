package com.jfinal.plugin.sqlinxml;

import com.jfinal.plugin.IPlugin;

public class SqlInXmlPlugin implements IPlugin {

	public SqlInXmlPlugin() {
		
	}
	public SqlInXmlPlugin(String string) {
	}

	@Override
	public boolean start() {
		try {
			SqlManager.init();
		} catch (Exception e) {
			new RuntimeException(e);
		}
		return true;
	}

	@Override
	public boolean stop() {
		SqlManager.clearSqlMap();
		return true;
	}

}
