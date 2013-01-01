package test.com.jfinal.ext.route;

public class CController extends BaseController {
	private final static String RESOURCE_PATH = "support/http/resources";

	public void index() {
		System.out.println(1);
		render("/nopermit.html");
	}

	

}
