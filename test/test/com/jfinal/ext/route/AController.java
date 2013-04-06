package test.com.jfinal.ext.route;

import com.jfinal.ext.route.ControllerBind;

@ControllerBind(controllerKey = "/aa", viewPath = "WEB-INF/")
// @ControllerBind(controllerKey = "/t")
public class AController extends BaseController {
    public void index() {
        setAttr("name", "zhoulei");
        setAttr("age", 10 * 2 + 4);
        render("add.html");
    }
}
