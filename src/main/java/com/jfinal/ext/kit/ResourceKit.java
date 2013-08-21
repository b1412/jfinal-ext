package com.jfinal.ext.kit;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;

public class ResourceKit {
    public static Map<String, String> readProperties(String resourceName) {
        Properties properties = new Properties();
        URL resource = Resources.getResource(resourceName);
        try {
            properties.load(new InputStreamReader(resource.openStream(), "UTF-8"));
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
        return Maps.fromProperties(properties);
    }

}
