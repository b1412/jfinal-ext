package test.com.jfinal.plugin.ext;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;

@ControllerBind(controllerKey = "/t")
public class AController extends Controller {
	public void index(){
		System.out.println("a index");
		setAttr("name", "zhoulei");
		setAttr("age", 10*2+4);
		render("add.html");
	}
}
