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
package com.jfinal.ext.interceptor;

import java.util.Map;

import com.google.common.collect.Maps;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.ext.render.exception.ExceptionRender;
import com.jfinal.render.RenderFactory;

public class ExceptionInterceptor implements Interceptor {
    private ExceptionRender defaultRender;
    private Map<Class<? extends Exception>, ExceptionRender> exceptionMapping = Maps.newHashMap();

    public ExceptionInterceptor setDefault(String defaultView) {
        this.defaultRender = new DefaultExceptionRender(defaultView);
        return this;
    }

    public ExceptionInterceptor setDefault(ExceptionRender defaultRender) {
        this.defaultRender = defaultRender;
        return this;
    }

    public ExceptionInterceptor addMapping(Class<? extends Exception> cause, String view) {
        exceptionMapping.put(cause, new DefaultExceptionRender(view));
        return this;
    }

    public ExceptionInterceptor addMapping(Class<? extends Exception> cause, ExceptionRender view) {
        exceptionMapping.put(cause, view);
        return this;
    }

    @Override
    public void intercept(ActionInvocation ai) {
        try {
            ai.invoke();
        } catch (Exception e) {
            ExceptionRender exceptionRender = matchRender(e);
            if (exceptionRender == null) {
                exceptionRender = defaultRender;
            }
            if (exceptionRender != null) {
                ai.getController().render(exceptionRender.setException(e));
            }
        }
    }

    private ExceptionRender matchRender(Exception e) {
        Class<?> clazz = e.getClass();
        Class<?> superclass = clazz.getSuperclass();
        ExceptionRender exceptionRender = null;
        while (superclass != null) {
            superclass = clazz.getSuperclass();
            exceptionRender = exceptionMapping.get(clazz);
            if (exceptionRender != null) {
                break;
            }
            clazz = superclass;
        }
        return exceptionRender;
    }

    @SuppressWarnings("serial")
    private class DefaultExceptionRender extends ExceptionRender {

        public DefaultExceptionRender(String view) {
            this.view = view;
        }

        @Override
        public void render() {
            RenderFactory.me().getRender(view).setContext(request, response).render();
        }

    }
}
