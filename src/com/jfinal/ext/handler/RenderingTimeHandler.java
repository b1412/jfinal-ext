package com.jfinal.ext.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;
/**
 *  <a href = http://www.oschina.net/question/173052_62229 />
 * @author kid
 *
 */
public class RenderingTimeHandler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, boolean[] isHandled) {
		long start = System.currentTimeMillis();
		nextHandler.handle(target, request, response, isHandled);
		long end = System.currentTimeMillis();
		System.out.println("rending time:"+(end - start)+"ms");
	}

}
