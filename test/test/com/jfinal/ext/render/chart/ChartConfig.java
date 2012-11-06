package test.com.jfinal.ext.render.chart;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;

public class ChartConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		me.setEncoding("utf-8");
		me.setDevMode(true);
//		me.setViewType(ViewType.JSP);
	}
   
	@Override
	public void configRoute(Routes me) {
		me.add("/chart",ChartController.class);
//		AutoControllerRegist.regist(me);
	}

	@Override
	public void configPlugin(Plugins me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configInterceptor(Interceptors me) {
	}

	@Override
	public void configHandler(Handlers me) {
		
	}
	
	public static void main(String[] args) {
		JFinal.start("WebRoot", 8080, "/", 5);
	}

}
