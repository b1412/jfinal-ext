package com.jfinal.ext.interceptor;

import com.jfinal.ext.render.exception.ExceptionRender;

@SuppressWarnings("serial")
public class ErrorRender extends ExceptionRender {
    private String projectName;

    public ErrorRender(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public void render() {
        System.out.println(projectName + "系统异常发送到监控系统...");
    }

}
