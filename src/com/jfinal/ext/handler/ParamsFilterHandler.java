package com.jfinal.ext.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

/**
 * 过滤请求参数 计算出形如:
 * users[0].age=11
 * users[0].name=1
 * users[1].age=22
 * users[1].name=2
 * 的大小并把key=users.size, value=2 设置request(setAttribute )中 
 * @author liyupeng
 *
 * 2012-8-9
 */
public class ParamsFilterHandler extends Handler{

	@Override
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, boolean[] isHandled) {
		@SuppressWarnings("unchecked")
		Set<String> keySet = request.getParameterMap().keySet();
		Map<String, Integer> paramSize = new HashMap<String, Integer>();
		Integer oldSizeValue = 0 ;
		Integer newSizeValue = 0;
		int bracketStartIndex = 0 ;//括号开始索引开始位置
		String modelName = "";
		for (String key : keySet) {
			bracketStartIndex = key.indexOf("[");
			if (bracketStartIndex != -1) {
				modelName = key.substring(0,bracketStartIndex);
				oldSizeValue =  paramSize.get(modelName + ".size");
				try {
					newSizeValue = Integer.valueOf(key.substring(bracketStartIndex + 1,key.indexOf("]")));
				} catch (Exception e) {
					throw new RuntimeException("param pattern error:" + key);
				}
				if (oldSizeValue != null) {
					newSizeValue = oldSizeValue > newSizeValue ? oldSizeValue : newSizeValue;
				}
				paramSize.put(modelName + ".size", newSizeValue);
			}
		}
		if(!paramSize.isEmpty()){
			keySet = paramSize.keySet();
			for (String key : keySet) {
				request.setAttribute(key, paramSize.get(key) + 1);
			}
		}
		nextHandler.handle(target, request, response, isHandled);

	}
}
