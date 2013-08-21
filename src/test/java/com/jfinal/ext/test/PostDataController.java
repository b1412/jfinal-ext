package com.jfinal.ext.test;

import java.io.InputStreamReader;

import com.google.common.io.CharStreams;
import com.jfinal.core.Controller;

public class PostDataController extends Controller {
    public void index() throws Exception {
        renderText(CharStreams.toString(new InputStreamReader(getRequest().getInputStream())));
    }
}
