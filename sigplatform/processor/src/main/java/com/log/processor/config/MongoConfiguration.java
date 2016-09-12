package com.log.processor.config;

import lombok.Data;

@Data
public class MongoConfiguration {
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}	
}
