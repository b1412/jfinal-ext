package com.jfinal.ext.route;

public class CController extends BaseController {

    public void index() {
        System.out.println(1);
        render("/nopermit.html");
    }

}
