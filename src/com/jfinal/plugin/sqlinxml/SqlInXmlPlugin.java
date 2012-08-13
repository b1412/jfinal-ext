package com.jfinal.plugin.sqlinxml;

import com.jfinal.plugin.IPlugin;

public class SqlInXmlPlugin implements IPlugin {

	@Override
	public boolean start() {
		try {
			SqlManager.parseSqlXml();
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
