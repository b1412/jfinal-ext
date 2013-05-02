package com.jfinal.ext.kit;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;

public class ResourceKit {
    public static Map<String, String> readProperties(String resourceName) {
        Properties properties = new Properties();
        try {
            URL resource = getResource(resourceName);
            properties.load(new InputStreamReader(resource.openStream(), "UTF-8"));
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
        return Maps.fromProperties(properties);
    }

    public static URL getResource(String resourceName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(resourceName);
        checkArgument(url != null, "resource %s not found.", resourceName);
        return url;
    }
}
