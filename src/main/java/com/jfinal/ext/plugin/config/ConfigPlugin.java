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
package com.jfinal.ext.plugin.config;

import java.util.List;

import com.google.common.collect.Lists;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;

public class ConfigPlugin implements IPlugin {

    static String suffix = "txt";

    protected final Logger logger = Logger.getLogger(getClass());

    private final List<String> includeResources = Lists.newArrayList();

    private final List<String> excludeResources = Lists.newArrayList();

    private boolean reload = true;

    public ConfigPlugin(String... includeResources) {
        if (includeResources != null) {
            for (String includeResource : includeResources) {
                this.includeResources.add(includeResource);
            }
        }
    }

    public ConfigPlugin excludeResource(String... resource) {
        if (includeResources != null) {
            for (String excludeResource : excludeResources) {
                excludeResources.add(excludeResource);
            }
        }
        return this;
    }

    public ConfigPlugin addResource(String resource) {
        includeResources.add(resource);
        return this;
    }

    public ConfigPlugin reload(boolean reload) {
        this.reload = reload;
        return this;
    }

    public static void setSuffix(String suffix) {
        ConfigPlugin.suffix = suffix;
    }

    @Override
    public boolean start() {
        ConfigKit.init(includeResources, excludeResources, reload);
        return true;
    }

    @Override
    public boolean stop() {
        return true;
    }

}
