
package com.jfinal.ext.log;

import com.jfinal.log.ILoggerFactory;
import com.jfinal.log.Logger;

/**
 * LogbackLoggerFactory.
 */
public class LogbackLoggerFactory implements ILoggerFactory {
	
	public Logger getLogger(Class<?> clazz) {
		return new LogbackLogger(clazz);
	}
	
	public Logger getLogger(String name) {
		return new LogbackLogger(name);
	}
}
