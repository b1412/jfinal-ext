package com.jfinal.ext.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.config.JFinalConfig;
import com.jfinal.core.JFinal;
import com.jfinal.handler.Handler;
import com.jfinal.log.Logger;

public class ControllerTestCase {

    protected final static Logger logger = Logger.getLogger(ControllerTestCase.class);
    protected static ServletContext servletContext = new MockServletContext();
    protected static MockHttpRequest request;
    protected static MockHttpResponse response;
    protected static Handler handler;

    public static void start(Class<? extends JFinalConfig> configClass) throws Exception {
        Class<JFinal> clazz = JFinal.class;
        JFinal me = JFinal.me();
        initConfig(clazz, me, servletContext, configClass.newInstance());
        Field field = me.getClass().getDeclaredField("handler");
        field.setAccessible(true);
        handler = (Handler) field.get(me);
    }

    public static String invoke(String url) throws Exception {
        return invoke(url, "");
    }

    public static String invoke(String url, File file) throws Exception {
        String body = "";
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                body += line + "\n";
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return invoke(url, body);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String invoke(String url, String body) throws Exception {
        request = new MockHttpRequest(body);
        response = new MockHttpResponse();
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
