package com.jfinal.ext.render.dwz;

import com.jfinal.core.Controller;
import com.jfinal.ext.kit.JfinalKit;
import com.jfinal.ext.render.DwzRender;

public class DwzController extends Controller {
    public void add() {
        User user = new User();
        user.set("name", "11");
        user.set("new3", "1");
        user.save();
    }

    public void delete() {
        int id = getParaToInt(0);
        if (id % 2 == 0) {
            render(DwzRender.success());
        } else {
            render(DwzRender.error("该记录已经删除"));
        }
    }

    public void restart() {
        JfinalKit.restartPlugin("active");
        renderNull();
    }
}
