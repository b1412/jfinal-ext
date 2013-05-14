package com.jfinal.ext.interceptor;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.render.ViewType;

public class Config extends JFinalConfig {

    @Override
    public void configConstant(Constants me) {
        me.setEncoding("utf-8");
        me.setDevMode(true);
        loadPropertyFile("classes/config.txt");
        me.setViewType(ViewType.JSP);
    }

    @Override
    public void configRoute(Routes me) {
        me.add("/p", TestController.class);
    }

    @Override
    public void configPlugin(Plugins me) {
    }

    @Override
    public void configInterceptor(Interceptors me) {
        ExceptionInterceptor exceptionInterceptor = new ExceptionInterceptor();
        exceptionInterceptor.addMapping(IllegalArgumentException.class, "/exceptions/a.html");
        exceptionInterceptor.addMapping(IllegalStateException.class, "exceptions/b.html");
        exceptionInterceptor.setDefault(new ErrorRender("测试系统"));
        I18nInterceptor i18nInterceptor = new I18nInterceptor();
        i18nInterceptor.setDefaultCountry("");
        me.add(i18nInterceptor);
        me.add(exceptionInterceptor);
    }

    @Override
    public void configHandler(Handlers me) {

    }

    public static void main(String[] args) {
        JFinal.start("WebRoot", 9090, "/", 5);
        // System.out.println(new Locale("cn"));
        // System.out.println(new Locale("tw"));
        // System.out.println(new Locale("en"));
    }

}
