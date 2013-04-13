package com.jfinal.ext.kit;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;

public class ResourceKit {
    public static Map<String, String> readProperties(String resourceName) {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(Resources.getResource(resourceName).openStream(), "UTF-8"));
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
        return Maps.fromProperties(properties);
    }
}
