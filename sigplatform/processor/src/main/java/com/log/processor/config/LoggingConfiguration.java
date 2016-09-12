package com.log.processor.config;

import lombok.Data;

@Data
public class LoggingConfiguration {
	private String level;
	private String loggers;
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getLoggers() {
		return loggers;
	}
	public void setLoggers(String loggers) {
		this.loggers = loggers;
	}
}
