package com.jfinal.ext.interceptor.excel;

import com.jfinal.kit.StrKit;

public class NotNullCellValidate implements CellValidate {

	@Override
	public boolean validate(Object obj) {
		return obj != null && StrKit.notBlank(obj.toString());
	}
}
