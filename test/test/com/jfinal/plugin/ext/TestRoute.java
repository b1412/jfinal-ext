package test.com.jfinal.plugin.ext;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import javax.servlet.ServletContext;

import org.junit.Test;

import com.jfinal.config.JFinalConfig;
import com.jfinal.core.JFinal;
public class TestRoute {

	@Test
	public void test() throws Exception {
		Class<JFinal> clazz = JFinal.class;
		JFinal me = JFinal.me();
		ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getRealPath("/")).thenReturn("/test"); 
		JFinalConfig config = new Config();
		Method method = clazz.getDeclaredMethod("init", JFinalConfig.class,ServletContext.class);
		method.setAccessible(true);
		method.invoke(me, config,servletContext);
	}

}
