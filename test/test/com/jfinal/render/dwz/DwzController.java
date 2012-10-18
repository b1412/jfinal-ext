package test.com.jfinal.render.dwz;

import com.jfinal.core.Controller;
import com.jfinal.render.DwzRender;

public class DwzController extends Controller {
	public void delete() {
		int id = getParaToInt(0);
		if (id % 2 == 0) {
			render(DwzRender.success());
		} else {
			render(DwzRender.error("该记录已经删除"));
		}
	}
}
