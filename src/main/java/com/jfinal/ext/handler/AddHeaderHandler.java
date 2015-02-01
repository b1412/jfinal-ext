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
package com.jfinal.ext.handler;

import com.google.common.collect.Maps;
import com.jfinal.handler.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Map.Entry;

public class AddHeaderHandler extends Handler {
    private final Map<String, Object> headers = Maps.newHashMap();

    public AddHeaderHandler addHeader(String name, String header) {
        headers.put(name, header);
        return this;
    }

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        for (Entry<String, Object> entry : headers.entrySet()) {
            Object header = entry.getValue();
            String name = entry.getKey();
            if (header instanceof String) {
                response.addHeader(name, (String) header);
            } else if (header instanceof Integer) {
                response.addIntHeader(name, (Integer) header);
            } else if (header instanceof Long) {
                response.addDateHeader(name, (Long) header);
            } else {
                new RuntimeException("Not support the name (" + name + ") with  value that type is "
                        + header.getClass());
            }
        }
        nextHandler.handle(target, request, response, isHandled);
    }
}
