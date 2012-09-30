package com.jfinal.ext.route;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import com.jfinal.plugin.tablebind.ClassSearcher;
import com.jfinal.util.StringKit;

public class AutoControllerRegist {
	private static Logger logger = LoggerFactory.getLogger(AutoControllerRegist.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void regist(Routes routes) {
		List<Class> controllerClasses = ClassSearcher.findInClasspath(Controller.class);
		ControllerBind controllerBind = null;
		for (Class controller : controllerClasses) {
			controllerBind = (ControllerBind) controller.getAnnotation(ControllerBind.class);
			if (controllerBind == null) {
				routes.add(controllerKey(controller), controller);
				logger.debug("routes.add({}, {})", controllerKey(controller), controller.getName());
			} else {
				logger.debug("routes.add({}, {},{})", new Object[] { controllerBind.controllerKey(), controller,
						controllerBind.viewPath() });
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
