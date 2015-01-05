package com.jfinal.ext.interceptor.excel.convert;

/**
 *  单元格值转换器
 * @author zhoulei
 *
 */
public interface CellConvert<T> {
	T convert(String val, T obj);
}
