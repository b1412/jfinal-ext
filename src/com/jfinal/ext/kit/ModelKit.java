package com.jfinal.ext.kit;

import java.util.Map.Entry;
import java.util.Set;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

public class ModelKit {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Record toRecord(Model model){
		Record record  = new Record();
		Set<Entry<String, Object>>  attrs = model.getAttrsEntrySet();
		for (Entry<String, Object> entry : attrs) {
			record.set(entry.getKey(), entry.getValue());
		}
		return record;
	}
	@SuppressWarnings("rawtypes")
	public static void set(Model model,Object ...attrsAndValues){
		int length = attrsAndValues.length;
		if (length%2!=0) {
			throw new IllegalArgumentException("attrsAndValues length must be even number");
		}
		for (int i = 0; i <length; i=i+2) {
			Object attr = attrsAndValues[i];
			if(!(attr instanceof String)){
				throw new IllegalArgumentException("the odd number of attrsAndValues  must be String");
			}
			model.set((String)attr, attrsAndValues[i+1]);
		}
	}
}
