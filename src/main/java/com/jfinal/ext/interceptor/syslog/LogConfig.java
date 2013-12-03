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
package com.jfinal.ext.interceptor.syslog;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 系统日志
 */
public class LogConfig {
    /** 日志描述*/
    String title;

    /**
     * key 请求参数 value 参数描述
     */
    Map<String, String> params = Maps.newHashMap();
    
    public LogConfig(String title) {
        this.title = title;
    }

    public LogConfig addPara(String key, String value) {
        params.put(key, value);
        return this;
    }

    public String getTitle() {
        return title;
    }

    public LogConfig setTitle(String title) {
        this.title = title;
        return this;
    }
}
