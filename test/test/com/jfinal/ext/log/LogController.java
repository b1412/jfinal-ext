package test.com.jfinal.ext.log;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;

public class LogController extends Controller {
	protected final Logger logger = Logger.getLogger(getClass());
	
	public void index() {
		logger.debug("test");
		renderNull();
	}
}
