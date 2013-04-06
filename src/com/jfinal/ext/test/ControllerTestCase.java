package com.jfinal.ext.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.config.JFinalConfig;
import com.jfinal.core.JFinal;
import com.jfinal.handler.Handler;

public class ControllerTestCase {
    protected static ServletContext servletContext = new MockServletContext();
    protected static MockHttpRequest request = new MockHttpRequest();
    protected static MockHttpResponse response = new MockHttpResponse();
    protected static Handler handler;

    public static void start(Class<? extends JFinalConfig> configClass) throws Exception {
        Class<JFinal> clazz = JFinal.class;
        JFinal me = JFinal.me();
        initConfig(clazz, me, servletContext, configClass.newInstance());
        Field field = me.getClass().getDeclaredField("handler");
        field.setAccessible(true);
        handler = (Handler) field.get(me);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String invoke(String url) throws Exception {
        Class handlerClazz = handler.getClass();
        Method handle = handlerClazz.getDeclaredMethod("handle", String.class, HttpServletRequest.class, HttpServletResponse.class,
                new boolean[] {}.getClass());
        handle.setAccessible(true);

        handle.invoke(handler, getTarget(url, request), request, response, new boolean[] { true });
        return "";
    }

    private static String getTarget(String url, MockHttpRequest request) {
        String target = url;
        if (url.contains("?")) {
            target = url.substring(0, url.indexOf("?"));
            String queryString = url.substring(url.indexOf("?") + 1);
            String[] keyVals = queryString.split("&");
            for (String keyVal : keyVals) {
                int i = keyVal.indexOf('=');
                String key = keyVal.substring(0, i);
                String val = keyVal.substring(i + 1);
                request.setParameter(key, val);
            }
        }
        return target;

    }

    public static Object findAttrAfterInvoke(String key) {
        return request.getAttribute(key);
    }

    private static void initConfig(Class<JFinal> clazz, JFinal me, ServletContext servletContext, JFinalConfig config) throws Exception {
        Method method = clazz.getDeclaredMethod("init", JFinalConfig.class, ServletContext.class);
        method.setAccessible(true);
        method.invoke(me, config, servletContext);
    }
}
