/**
 * Copyright (c) 2011-2013, kidzhou 周磊 (zhouleib1412@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jfinal.ext.plugin.jms;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import com.jfinal.ext.plugin.config.ConfigKit;
import com.jfinal.ext.plugin.config.ConfigPlugin;

public class JmsConfig {
    private static Properties properties;

    private static ConfigPlugin configPlugin;

    public static synchronized  void init(String resoruceLocation) {
        if (properties != null) {
            return;
        }
        InputStream is = JmsPlugin.class.getClassLoader().getResourceAsStream(resoruceLocation);
        if (is == null) {
            throw new RuntimeException("cant find properties in location :" + resoruceLocation);
        }
        properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("cant load properties in location :" + resoruceLocation, e);
        }
    }
    public static int getInt(String key) {
        return Integer.parseInt(getStr(key));
    }

    public static String getStr(String key) {
        if (configPlugin != null) {
            return ConfigKit.getStr(key);
        }
        Object objVal = properties.get(key);
        return objVal == null ? "" : objVal + "";
    }

    public static Set<String> keys() {
        Set<String> keySet = new HashSet<String>();
        for (Object objVal : properties.keySet()) {
            keySet.add(objVal + "");
        }
        return keySet;
    }

    public static void init(String resoruceLocation, ConfigPlugin configPlugin) {
        JmsConfig.configPlugin = configPlugin;
        init(resoruceLocation);
    }

}
