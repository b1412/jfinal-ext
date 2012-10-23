package com.jfinal.ext.route;

import java.util.List;

import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import com.jfinal.plugin.tablebind.ClassSearcher;
import com.jfinal.util.StringKit;

public class AutoControllerRegist {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void regist(Routes routes) {
		List<Class> controllerClasses = ClassSearcher.findInClasspath(Controller.class);
		ControllerBind controllerBind = null;
		for (Class controller : controllerClasses) {
			controllerBind = (ControllerBind) controller.getAnnotation(ControllerBind.class);
			if (controllerBind == null) {
				routes.add(controllerKey(controller), controller);
				System.out.println("routes.add("+controllerKey(controller)+", "+controller.getName()+")");
			} else if(StringKit.isBlank(controllerBind.viewPath())){
				routes.add(controllerBind.controllerKey(), controller);
				System.out.println("routes.add("+controllerBind.controllerKey()+", "+controller.getName()+")");
			}else{
				routes.add(controllerKey(controller), controller,controllerBind.viewPath());
				System.out.println("routes.add("+controllerBind.controllerKey()+", "+controller+","+controllerBind.viewPath()+")");
			}
		}
	}

	private static String controllerKey(Class clazz) {
		if (!clazz.getSimpleName().endsWith("Controller")) {
			throw new RuntimeException(clazz + " don,t has a ControllerBind annotation and it,s don,t end Controller! ");
		}
		String controllerKey = "/" + StringKit.firstCharToLowerCase(clazz.getSimpleName());
		controllerKey = controllerKey.substring(0, controllerKey.indexOf("Controller"));
		return controllerKey;
	}
}
