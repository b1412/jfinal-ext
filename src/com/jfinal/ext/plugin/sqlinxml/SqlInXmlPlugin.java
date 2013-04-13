package com.jfinal.ext.plugin.sqlinxml;

import com.jfinal.plugin.IPlugin;

public class SqlInXmlPlugin implements IPlugin {

    public SqlInXmlPlugin() {
    }

    @Override
    public boolean start() {
        SqlKit.init();
        return true;
    }

    @Override
    public boolean stop() {
        SqlKit.clearSqlMap();
        return true;
    }

}
