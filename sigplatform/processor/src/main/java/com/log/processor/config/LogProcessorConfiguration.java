package com.log.processor.config;

import io.dropwizard.Configuration;
import io.dropwizard.client.HttpClientConfiguration;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;

public class LogProcessorConfiguration extends Configuration {
	@NotEmpty
	@JsonProperty
	private String template;
	
	@NotEmpty
	@JsonProperty
	private String defaultName = "Sigtrak";
	
	@JsonProperty
    public String getTemplate() {
        return template;
    }

    @JsonProperty
    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    @JsonProperty
    public void setDefaultName(String name) {
        this.defaultName = name;
    }
	
	@JsonProperty
	private HttpClientConfiguration httpClient;
	
	public HttpClientConfiguration getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClientConfiguration httpClient) {
		this.httpClient = httpClient;
	}
	
	@JsonProperty("mongo")
	private MongoConfiguration mongoConfiguration;
	
	public MongoConfiguration getMongoConfiguration() {
		return mongoConfiguration;
	}

	public void setMongoConfiguration(MongoConfiguration mongoConfiguration) {
		this.mongoConfiguration = mongoConfiguration;
	}
	
	@JsonProperty("s3")
	private S3Configuration s3Configuration;
	
	public S3Configuration getS3Configuration() {
		return s3Configuration;
	}

	public void setS3Configuration(S3Configuration s3Configuration) {
		this.s3Configuration = s3Configuration;
	}
	
	@JsonProperty("job")
	private JobConfiguration jobConfiguration;

	public JobConfiguration getJobConfiguration() {
		return jobConfiguration;
	}

	public void setJobConfiguration(JobConfiguration jobConfiguration) {
		this.jobConfiguration = jobConfiguration;
	}
	
	@JsonProperty("file")
	private FileConfiguration fileConfiguration;

	public FileConfiguration getFileConfiguration() {
		return fileConfiguration;
	}

	public void setFileConfiguration(FileConfiguration fileConfiguration) {
		this.fileConfiguration = fileConfiguration;
	}
	
	@JsonProperty("logging")
	private LoggingConfiguration loggingConfiguration;

	public LoggingConfiguration getLoggingConfiguration() {
		return loggingConfiguration;
	}

	public void setLoggingConfiguration(LoggingConfiguration loggingConfiguration) {
		this.loggingConfiguration = loggingConfiguration;
	}
}
