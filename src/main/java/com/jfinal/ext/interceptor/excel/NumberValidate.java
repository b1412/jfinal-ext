package com.jfinal.ext.interceptor.excel;

public class NumberValidate implements CellValidate {

	public boolean validate(Object obj) {
		String str = obj + "";
		if (str.length() < 5) {
			return false;
		}
		return true;
	}

}
