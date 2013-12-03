package com.jfinal.ext.interceptor.excel;

import java.util.List;

public interface PostListProcessor<T>{
	void process(List<T> list) ;
}
