
package com.jfinal.ext.log;

import org.slf4j.spi.LocationAwareLogger;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.CoreConstants;

import com.jfinal.log.Logger;

/**
 * LogbackLogger.
 */
public class LogbackLogger extends Logger {
	
	private ch.qos.logback.classic.Logger log;
	private static final String callerFQCN = LogbackLogger.class.getName();
	
	LogbackLogger(Class<?> clazz) {
		LoggerContext loggerContext = new LoggerContext();
		loggerContext.setName(CoreConstants.DEFAULT_CONTEXT_NAME);
		log =  loggerContext.getLogger(clazz);
	}
	
	LogbackLogger(String name) {
		log =  new LoggerContext().getLogger(name);
	}
	
	public void info(String message) {
		log.info(message);
//		log.log(null, callerFQCN, LocationAwareLogger.INFO_INT, message, new Object[]{}, null);
	}
	
	public void info(String message, Throwable t) {
		log.log(null, callerFQCN, LocationAwareLogger.INFO_INT, message, new Object[]{}, t);
	}
	
	public void debug(String message) {
		log.log(null, callerFQCN, LocationAwareLogger.DEBUG_INT, message, new Object[]{}, null);
	}
	
	public void debug(String message, Throwable t) {
		log.log(null, callerFQCN, LocationAwareLogger.DEBUG_INT, message, new Object[]{}, t);
	}
	
	public void warn(String message) {
		log.log(null, callerFQCN, LocationAwareLogger.WARN_INT, message, new Object[]{}, null);
	}
	
	public void warn(String message, Throwable t) {
		log.log(null, callerFQCN, LocationAwareLogger.WARN_INT, message, new Object[]{}, t);
	}
	
	public void error(String message) {
		log.log(null, callerFQCN, LocationAwareLogger.ERROR_INT, message, new Object[]{}, null);
	}
	
	public void error(String message, Throwable t) {
		log.log(null, callerFQCN, LocationAwareLogger.ERROR_INT, message, new Object[]{}, t);
	}
	/**
	 * LogbackLogger fatal is the same as the error.
	 */
	public void fatal(String message) {
		log.log(null, callerFQCN, LocationAwareLogger.ERROR_INT, message, new Object[]{}, null);
	}
	/**
	 * LogbackLogger fatal is the same as the error.
	 */
	public void fatal(String message, Throwable t) {
		log.log(null, callerFQCN, LocationAwareLogger.ERROR_INT, message, new Object[]{}, t);
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



