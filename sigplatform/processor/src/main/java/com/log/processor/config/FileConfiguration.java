package com.log.processor.config;

import lombok.Data;

@Data
public class FileConfiguration {
	private String directory;
	private String name;
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
