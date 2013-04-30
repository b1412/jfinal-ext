package com.jfinal.ext.render.exception;

import com.jfinal.render.Render;

public abstract class ExceptionRender extends Render {
    private static final long serialVersionUID = -7908640392524128432L;
    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public ExceptionRender setException(Exception exception) {
        this.exception = exception;
        return this;
    }

}
