package com.jfinal.ext.util;

import com.jfinal.plugin.activerecord.Model;

public class ModelHelper {
	@SuppressWarnings("rawtypes")
	public static void set(Model model,Object ...attrsAndValues){
		int length = attrsAndValues.length;
		if (length%2!=0) {
			throw new IllegalArgumentException("attrsAndValues length must be even number");
		}
		for (int i = 0; i <length; i=i+2) {
			Object attr = attrsAndValues[i];
			if(!(attr instanceof String)){
				throw new IllegalArgumentException("the odd number of attrsAndValues  must be String");
			}
			model.set((String)attr, attrsAndValues[i+1]);
		}
	}
}
