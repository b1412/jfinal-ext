package com.jfinal.ext.interceptor.excel;

/**
 *  单元格值转换器
 * @author zhoulei
 *
 */
public interface CellConvert {
	Object convert(Object val, Object obj);
}
