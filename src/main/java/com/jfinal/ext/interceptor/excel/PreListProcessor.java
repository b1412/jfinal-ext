package com.jfinal.ext.interceptor.excel;

import java.util.List;

public interface PreListProcessor<T>{
	void process(List<T> list);
}
