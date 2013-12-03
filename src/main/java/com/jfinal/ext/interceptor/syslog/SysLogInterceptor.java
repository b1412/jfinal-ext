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
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Maps;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.ext.kit.ServletKit;
import com.jfinal.kit.StringKit;

public class SysLogInterceptor implements Interceptor {
    Map<String, LogConfig> acitonLogs = Maps.newHashMap();
    LogProcessor logProcesser = null;

    public SysLogInterceptor setLogProcesser(LogProcessor logProcesser) {
        this.logProcesser = logProcesser;
        return this;
    }

    public SysLogInterceptor addConfig(String actionKey, LogConfig log) {
        acitonLogs.put(actionKey, log);
        return this;
    }

    @Override
    public void intercept(ActionInvocation ai) {
        String actionKey = ai.getActionKey();
        Controller c = ai.getController();
        LogConfig log = acitonLogs.get(actionKey);
        if (log != null) {
            logFromConfig(c, log);
        }
        ai.invoke();
    }

    private void logFromConfig(Controller c, LogConfig log) {
        SysLog sysLog = new SysLog();
        sysLog.ip = ServletKit.getIp(c.getRequest());
        sysLog.user = logProcesser.getUsername(c);
        Map<String, String[]> parameterMap = c.getRequest().getParameterMap();
        Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
        Map<String, String> paraMap = Maps.newHashMap();
        for (Entry<String, String[]> entry : entrySet) {
            String key = entry.getKey();
            String value = entry.getValue()[0];
            String result = log.params.get(key);
            if (StringKit.isBlank(result))
                continue;
            paraMap.put(result, value);
        }
        sysLog.message = logProcesser.formatMessage(log.title, paraMap);
        logProcesser.process(sysLog);
    }

}
