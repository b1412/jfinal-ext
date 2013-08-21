package com.jfinal.ext.render.csv;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.log.Log4jLoggerFactory;

public class CsvConfig extends JFinalConfig {

    @Override
    public void configConstant(Constants me) {
        me.setDevMode(true);
        me.setLoggerFactory(new Log4jLoggerFactory());
    }

    @Override
    public void configRoute(Routes me) {
        me.add("/csv", CSVController.class);
    }

    @Override
    public void configPlugin(Plugins me) {

    }

    @Override
    public void configInterceptor(Interceptors me) {
    }

    @Override
    public void configHandler(Handlers me) {

    }

    public static void main(String[] args) {
        JFinal.start("WebRoot", 8080, "/", 5);
    }

}
