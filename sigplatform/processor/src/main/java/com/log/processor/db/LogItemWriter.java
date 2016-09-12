package com.log.processor.db;

import com.log.processor.core.LogItem;
import com.amazonaws.services.s3.AmazonS3;
import com.log.processor.core.S3Api;

public interface LogItemWriter {
	public void writeLogItem( LogItem item, String directory, String name );
	public void writeLogItem( LogItem item, S3Api s3Api, AmazonS3 s3, String bucketName, String key );
}
