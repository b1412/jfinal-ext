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
