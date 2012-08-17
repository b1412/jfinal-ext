package com.jfinal.plugin.guice;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.google.inject.Guice;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

public class IocInterceptor implements Interceptor {

	@Override
	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();
		Field[] fields = controller.getClass().getDeclaredFields();
		for (Field field : fields){
			if(Modifier.isFinal(field.getModifiers())){
				continue;
			}
			injectField(controller, field);
		}
		ai.invoke();
	}

	private void injectField(Controller controller, Field field) {
		try {
			field.setAccessible(true);
			field.set(controller, Guice.createInjector( ).getInstance(field.getType()));
			field.setAccessible(false);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
