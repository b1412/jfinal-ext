package com.jfinal.ext.route;

public @interface ControllerBind {
	public String controllerKey() ;
	public String viewPath() default"";
}
