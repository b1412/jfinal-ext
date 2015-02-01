package com.jfinal.ext.render.dwz;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.I18nInterceptor;
import com.jfinal.ext.kit.JFinalKit;

public class DwzController extends Controller {
    public void add() {
        User user = new User();
        user.set("name", "11");
        user.set("new3", "1");
        user.save();
    }

    @Before(I18nInterceptor.class)
    public void delete() {
        int id = getParaToInt(0);
//        if (id % 2 == 0) {
//            render(DwzRender.success());
//        } else {
//            render(DwzRender.error("该记录已经删除"));
//        }
        render("list.html");
    }

    public void restart() {
        JFinalKit.restartPlugin("active");
        renderNull();
    }
}
