package com.jfinal.ext.interceptor.excel;

import com.jfinal.kit.StringKit;

public class NotNullCellValidate implements CellValidate {

	@Override
	public boolean validate(Object obj) {
		return obj != null && StringKit.notBlank(obj.toString());
	}
}
