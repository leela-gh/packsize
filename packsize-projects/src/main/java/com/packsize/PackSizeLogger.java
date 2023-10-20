package com.packsize;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PackSizeLogger {
	
	private static final Logger logger = LogManager.getLogger(PackSizeLogger.class);
	
	public static void info(String msg) {
		logger.info(msg);
	}
	
	public static void warn(String msg) {
		logger.warn(msg);
	}
	
	public static void error(String msg) {
		logger.error(msg);
	}
	
	public static void traceEntry() {
		logger.traceEntry();
	}
	
	public static void fatal(String msg) {
		logger.fatal(msg);
	}
	
	public static void exit() {
		logger.traceExit();
	}
}
