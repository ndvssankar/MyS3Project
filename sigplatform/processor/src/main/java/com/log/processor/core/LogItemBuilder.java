package com.log.processor.core;

import java.io.File;
import com.amazonaws.services.s3.AmazonS3;

public interface LogItemBuilder {
	public LogItem buildLogItem(File logFile);
	public LogItem buildLogItem(S3Api s3Api, AmazonS3 s3, String bucketName, String key);
}
