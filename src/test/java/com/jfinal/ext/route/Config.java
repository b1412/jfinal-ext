package com.jfinal.ext.route;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.plugin.tablebind.AutoTableBindPlugin;
import com.jfinal.plugin.activerecord.tx.TxByRegex;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;

public class Config extends JFinalConfig {

    public static void main(String[] args) {
        JFinal.start("WebRoot", 8080, "/", 5);
    }

    @Override
    public void configConstant(Constants me) {
        me.setEncoding("utf-8");
        me.setDevMode(true);
        me.setViewType(ViewType.JSP);
    }

    @Override
    public void configHandler(Handlers me) {

    }

    @Override
    public void configInterceptor(Interceptors me) {
        me.add(new TxByRegex(".*.save"));
    }

    @Override
    public void configPlugin(Plugins me) {
        DruidPlugin druidPlugin = new DruidPlugin("jdbc:mysql://127.0.0.1/jfinal_demo", "root", "root");

        AutoTableBindPlugin atbp = new AutoTableBindPlugin(druidPlugin).autoScan(false);
        me.add(druidPlugin);
        me.add(atbp);
    }

    @Override
    public void configRoute(Routes me) {
        AutoBindRoutes routes = new AutoBindRoutes();
        me.add(routes);
    }

}
