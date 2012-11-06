
package com.jfinal.ext.log;

import com.jfinal.log.Logger;

/**
 * Slf4jLogger.
 */
public class Slf4jLogger extends Logger {
	
	private org.slf4j.Logger log;
	private static final String callerFQCN = Slf4jLogger.class.getName();
	
	Slf4jLogger(Class<?> clazz) {
		log = org.slf4j.LoggerFactory.getLogger(clazz);
	}
	
	Slf4jLogger(String name) {
		log = org.slf4j.LoggerFactory.getLogger(name);
	}
	
	public void info(String message) {
		log.info(message);
	}
	
	public void info(String message, Throwable t) {
		log.info(message,t);
	}
	
	public void debug(String message) {
		log.debug(message);
	}
	
	public void debug(String message, Throwable t) {
		log.debug(message, t);
	}
	
	public void warn(String message) {
		log.warn(message);
	}
	
	public void warn(String message, Throwable t) {
		log.warn(message, t);
	}
	
	public void error(String message) {
		log.equals(message);
	}
	
	public void error(String message, Throwable t) {
		log.error(message,t);
	}
	/**
	 * Slf4jLogger fatal is the same as the error.
	 */
	public void fatal(String message) {
		log.equals(message);
	}
	/**
	 * Slf4jLogger fatal is the same as the error.
	 */
	public void fatal(String message, Throwable t) {
		log.error(message,t);
	}
	
	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}
	
	public boolean isInfoEnabled() {
		return log.isInfoEnabled();
	}
	
	public boolean isWarnEnabled() {
		return log.isWarnEnabled();
	}
	
	public boolean isErrorEnabled() {
		return log.isErrorEnabled();
	}
	
	/**
	 * Slf4jLogger fatal is the same as the error.
	 */
	public boolean isFatalEnabled() {
		return log.isErrorEnabled();
	}
}

