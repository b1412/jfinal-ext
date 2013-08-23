package com.jfinal.ext.render.chart;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.log.Log4jLoggerFactory;

public class ChartConfig extends JFinalConfig {

    public static void main(String[] args) {
        JFinal.start("WebRoot", 8080, "/chart", 5);
    }

    @Override
    public void configConstant(Constants me) {
        me.setEncoding("utf-8");
        me.setDevMode(true);
        me.setLoggerFactory(new Log4jLoggerFactory());
        // me.setLoggerFactory(new LogbackLoggerFactory());
    }

    @Override
    public void configHandler(Handlers me) {

    }

    @Override
    public void configInterceptor(Interceptors me) {
    }

    @Override
    public void configPlugin(Plugins me) {

    }

    @Override
    public void configRoute(Routes me) {
        me.add("/chart", ChartController.class);
        // AutoControllerRegist.regist(me);
    }

}
