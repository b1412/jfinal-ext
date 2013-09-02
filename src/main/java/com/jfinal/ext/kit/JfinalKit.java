package com.jfinal.ext.kit;

import java.util.List;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;

public class JfinalKit {

    protected static final  Logger LOG = Logger.getLogger(JfinalKit.class);

    static {
        init();
    }
    private static List<IPlugin> pluginList;
    private static Constants constants;
    private static Routes routes;
    private static Plugins plugins;
    private static Interceptors interceptors;
    private static Handlers handlers;

    public static void init() {
        Reflect reflect = Reflect.on("com.jfinal.core.Config");
        constants = reflect.get("constants");
        routes = reflect.get("routes");
        plugins = reflect.get("plugins");
        interceptors = reflect.get("interceptors");
        handlers = reflect.get("handlers");
        pluginList = plugins.getPluginList();
    }

    public static Constants getConstants() {
        return JfinalKit.constants;
    }

    public static Routes getRoutes() {
        return JfinalKit.routes;
    }

    public static Plugins getPlugins() {
        return JfinalKit.plugins;
    }

    public static Interceptors getInterceptors() {
        return JfinalKit.interceptors;
    }

    public static Handlers getHandlers() {
        return JfinalKit.handlers;
    }

    public static void stopPlugin(String pluginName) {
        for (IPlugin iPlugin : pluginList) {
            if (iPlugin.getClass().getSimpleName().equals(pluginName)) {
                iPlugin.stop();
            }
        }
    }

    public static void startPlugin(String pluginName) {
        for (IPlugin iPlugin : pluginList) {
            if (iPlugin.getClass().getSimpleName().equals(pluginName)) {
                iPlugin.start();
            }
        }
    }

    public static void restartPlugin(String pluginName) {
        for (IPlugin iPlugin : pluginList) {
            if (iPlugin.getClass().getSimpleName().equals(pluginName)) {
                iPlugin.stop();
                iPlugin.start();
            }
        }
    }

}
