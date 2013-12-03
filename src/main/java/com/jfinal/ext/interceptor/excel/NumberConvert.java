package com.jfinal.ext.interceptor.excel;

import org.apache.commons.lang.StringUtils;

public class NumberConvert implements CellConvert {

	public Object convert(Object val,Object obj) {
		if (!StringUtils.isNumeric(obj.toString())) {
			if(isNotDouble(obj))
			obj = "0";
		}
		return obj;
	}

	private boolean isNotDouble(Object obj) {
		try {
			Double.parseDouble(obj.toString());
		} catch (NumberFormatException e) {
			return true;
		}
		return false;
	}

}
