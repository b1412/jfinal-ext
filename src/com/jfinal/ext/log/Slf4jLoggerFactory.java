
package com.jfinal.ext.log;

import com.jfinal.log.ILoggerFactory;
import com.jfinal.log.Logger;

/**
 * Slf4jLoggerFactory.
 */
public class Slf4jLoggerFactory implements ILoggerFactory {
	
	public Logger getLogger(Class<?> clazz) {
		return new Slf4jLogger(clazz);
	}
	
	public Logger getLogger(String name) {
		return new Slf4jLogger(name);
	}
}
