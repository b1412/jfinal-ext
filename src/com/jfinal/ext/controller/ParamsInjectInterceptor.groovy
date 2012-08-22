package com.jfinal.ext.controller;


import java.util.Enumeration;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

public class ParamsInjectInterceptor implements Interceptor {

	@Override
	public void intercept(ActionInvocation ai) {
		Controller c = ai.getController();
		def allMap = c.getParaNames().inject([:]) {map,it->
			map+=[(it):c.getPara(it)];
		}
		c.getParaNames().inject([:]) {map,it->
			map+=[(it):c.getPara(it)];
		}
		def simpleMap = allMap.grep{!it.key.contains(".")};
		simpleMap.each {c.metaClass."${it.key}"=it.value};
		def pointMap = allMap.grep{it.key.contains(".")};
		def mapDefs =pointMap.collect {it.key[0..it.key.indexOf(".")-1]}.unique();
		mapDefs.each { mapDef ->
			def temp = pointMap.grep{pointKey ->pointKey.key.startsWith(mapDef)}
			.inject ([:]){sum,it-> sum<<[(it.key[it.key.indexOf(".")+1..-1]):it.value]}
			c.metaClass."${mapDef}"=temp;
			c.setAttr("${mapDef}", temp)
		}

		ai.invoke();
	}
}
