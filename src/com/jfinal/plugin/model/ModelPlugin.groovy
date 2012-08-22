package com.jfinal.plugin.model;

import java.lang.reflect.Method

import com.jfinal.plugin.IPlugin
import com.jfinal.plugin.activerecord.Model

public class ModelPlugin implements IPlugin {

	@Override
	public boolean start() {
		Model.metaClass.propertyMissing={
			name,value-> 
			Method setMethod =  Model.class.methods.find {it.name=="set"}
			setMethod.invoke(this, name,value);
		}
		Model.metaClass.propertyMissing={
			name-> 
			Method setMethod =  Model.class.methods.find {it.name=="get"}
			setMethod.invoke(this, name);
		}
		return true;
	}

	@Override
	public boolean stop() {
		return true;
	}

}
