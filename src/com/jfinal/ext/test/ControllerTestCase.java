package com.jfinal.ext.test;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.ServletContext;

import org.joor.Reflect;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.jfinal.config.JFinalConfig;
import com.jfinal.core.JFinal;
import com.jfinal.handler.Handler;
import com.jfinal.log.Logger;

public class ControllerTestCase {

    protected static final Logger LOG = Logger.getLogger(ControllerTestCase.class);
    protected static ServletContext servletContext = new MockServletContext();;
    protected static MockHttpRequest request;
    protected static MockHttpResponse response;
    protected static Handler handler;

    public static void start(Class<? extends JFinalConfig> configClass) throws Exception {
        Class<JFinal> clazz = JFinal.class;
        JFinal me = JFinal.me();
        initConfig(clazz, me, servletContext, configClass.newInstance());
        handler = Reflect.on(me).get("handler");
    }

    public static String invoke(String url) throws Exception {
        return invoke(url, "");
    }

    public static String invoke(String url, File reqFile) {
        return invoke(url, reqFile, null);
    }

    public static String invoke(String url, String body, File respFile) {
        String resp = invoke(url, body);
        if (respFile != null) {
            try {
                Files.write(resp, respFile, Charsets.UTF_8);
            } catch (IOException e) {
                Throwables.propagate(e);
            }
        }
        return resp;
    }

    public static String invoke(String url, File reqFile, File respFile) {
        List<String> req = Lists.newArrayList();
        try {
            req = Files.readLines(reqFile, Charsets.UTF_8);
        } catch (IOException e) {
            Throwables.propagate(e);
        }
        String resp = invoke(url, Joiner.on("").join(req));
        if (respFile != null) {
            try {
                Files.write(resp, respFile, Charsets.UTF_8);
            } catch (IOException e) {
                Throwables.propagate(e);
            }
        }
        return resp;
    }

    public static String invoke(String url, String body) {
        StringWriter resp = new StringWriter();
        request = new MockHttpRequest(body);
        response = new MockHttpResponse(resp);
        if (handler == null) {
            System.err.println("请在ControllerTestCase的子类里面添加如下方法，YourConfig为你想测试的Config");
            System.err.println("    @BeforeClass ");
            System.err.println("    public static void init() throws Exception {");
            System.err.println("        start(YourConfig.class)");
            System.err.println("    }");
            return "";
        }
        Reflect.on(handler).call("handle", getTarget(url, request), request, response, new boolean[] { true });
        return resp.toString();
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

    private static void initConfig(Class<JFinal> clazz, JFinal me, ServletContext servletContext, JFinalConfig config) {
        Reflect.on(me).call("init", config, servletContext);
    }
}
