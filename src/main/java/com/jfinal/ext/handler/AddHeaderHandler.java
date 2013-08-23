package com.jfinal.ext.handler;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Maps;
import com.jfinal.handler.Handler;

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
                new RuntimeException("does not support the name (" + name + ") with  value that type is "
                        + header.getClass());
            }
        }
        nextHandler.handle(target, request, response, isHandled);
    }
}
