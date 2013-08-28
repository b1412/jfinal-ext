package com.jfinal.ext.render.excel;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.plugin.tablebind.Blog;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;

public class ExcelConfig extends JFinalConfig {

    public static void main(String[] args) {
        JFinal.start("src/main/webapp", 8080, "/", 0);
    }

    @Override
    public void configConstant(Constants me) {
        me.setEncoding("utf-8");
        me.setDevMode(true);
        loadPropertyFile("classes/config.txt");
    }

    @Override
    public void configHandler(Handlers me) {

    }

    @Override
    public void configInterceptor(Interceptors me) {
    }

    @Override
    public void configPlugin(Plugins me) {
//        DruidPlugin db = new DruidPlugin(getProperty("url"), "root", "root");
//        ActiveRecordPlugin arp = new ActiveRecordPlugin(db);
//        arp.addMapping("blog", Blog.class);
//        me.add(db);
//        me.add(arp);
    }

    @Override
    public void configRoute(Routes me) {
        me.add("/poi", PoiController.class);
        me.add("/jxls", JxlsController.class);
    }

}
