package com.jfinal.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jfinal.config.JFinalConfig;
import com.jfinal.core.JFinal;
import com.jfinal.handler.Handler;

public class ControllerTestCase {
	protected static ServletContext servletContext = mock(ServletContext.class);
	protected static HttpServletRequest request = new MockHttpRequest();
	protected static HttpServletResponse response = new MockHttpResponse();
	protected static Handler handler;

	public static void start(JFinalConfig config) throws Exception {
		Class<JFinal> clazz = JFinal.class;
		JFinal me = JFinal.me();
		when(servletContext.getRealPath("/")).thenReturn("/test");
		initConfig(clazz, me, servletContext, config);
		Field field = me.getClass().getDeclaredField("handler");
		field.setAccessible(true);
		handler = (Handler) field.get(me);
	}

	public static String invoke(String path) throws Exception {
		Class handlerClazz = handler.getClass();
		Method handle = handlerClazz.getDeclaredMethod("handle", String.class,
				HttpServletRequest.class, HttpServletResponse.class,
				new boolean[] {}.getClass());
		handle.setAccessible(true);
		handle.invoke(handler, path, request, response, new boolean[] { true });
		return "";
	}

	public static Object findAttrAfterInvoke(String key) {
		return request.getAttribute(key);
	}

	private static void initConfig(Class<JFinal> clazz, JFinal me,
			ServletContext servletContext, JFinalConfig config)
			throws Exception {
		Method method = clazz.getDeclaredMethod("init", JFinalConfig.class,
				ServletContext.class);
		method.setAccessible(true);
		method.invoke(me, config, servletContext);
	}
}