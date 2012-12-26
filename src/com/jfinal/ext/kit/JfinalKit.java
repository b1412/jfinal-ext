package com.jfinal.ext.kit;

import java.lang.reflect.Field;
import java.util.List;

import com.jfinal.config.Plugins;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;

public class JfinalKit {
    
    protected final static Logger logger = Logger.getLogger(JfinalKit.class);

    static {
        init();
    }
    private static List<IPlugin> pluginList;

    public static void init() {
        Field field;
        Plugins plugins = null;
        try {
            field = Class.forName("com.jfinal.core.Config").getDeclaredField("plugins");
            field.setAccessible(true);
            plugins = (Plugins) field.get(null);
            field.setAccessible(false);
        } catch (Exception e) {
            logger.error("get plugins error", e);
        }
        pluginList = plugins.getPluginList();
        
        
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
