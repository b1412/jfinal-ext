package com.jfinal.ext.interceptor.excel;

/**
 * 单元格值合法性验证
 * @author zhoulei
 *
 */
public interface CellValidate {
	boolean validate(Object obj);
}
