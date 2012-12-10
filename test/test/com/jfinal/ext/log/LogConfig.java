package test.com.jfinal.ext.log;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.log.Log4jLoggerFactory;
import com.jfinal.plugin.activerecord.tx.TxByRegex;

public class LogConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		me.setEncoding("utf-8");
		me.setDevMode(true);
		me.setLoggerFactory(new Log4jLoggerFactory());
	}
   
	@Override
	public void configRoute(Routes me) {
		me.add("/",LogController.class);
	}

	@Override
	public void configPlugin(Plugins me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configInterceptor(Interceptors me) {
		me.add(new TxByRegex(".*.save"));
	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub
		
	}

}
