package com.log.processor.config;

import lombok.Data;

@Data
public class JobConfiguration {
	private int numJobs;

	public int getNumJobs() {
		return numJobs;
	}
	public void setNumJobs(int jobs) {
		this.numJobs = jobs;
	}
}
