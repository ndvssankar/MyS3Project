package com.log.processor.core;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.log.processor.common.CommonUtils;
import com.log.processor.db.LogItemWriter;

import static org.slf4j.LoggerFactory.getLogger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;

import com.log.processor.core.AbstractLogProcessor;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;

public class S3LogFileProcessor extends AbstractLogProcessor implements Runnable {
	private static final Logger log = getLogger(LogFileProcessor.class);
	private LogItemWriter writer;
	private String bucketName;
	private LogItemBuilder builder;
	private AmazonS3 s3;
	private S3Api s3Api;

	@Inject
	public S3LogFileProcessor( @Named("bucket")  String bucketName, LogItemBuilder builder, LogItemWriter writer  ) {
		this.bucketName = bucketName;
		this.builder = builder;
		this.writer = writer;
		System.out.println("Control Here - 6");
		try {
			this.s3Api = new S3Api();
			this.s3 = s3Api.getConnection();
		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
			+ "to Amazon S3, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
			+ "a serious internal problem while trying to communicate with S3, "
			+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}

	@Override
	public void run() {
		log.info("Entered LogFileProcessor...");
		while(true) {
			try {
				process();
				Thread.sleep(200);
			} catch(Exception e) {
				log.error("Exception in LogReader thread. [Exception={}] ", e);
			}
		}
	}

	public void process() {
		System.out.println("Control in Process : " + bucketName);
		List<S3ObjectSummary> objectListing = new S3Api().getObjectsFromBucket(s3, bucketName);
		if(objectListing != null) {
			System.out.println("Object List is ");
			for (S3ObjectSummary objectSummary : objectListing) {
				if(objectSummary.getKey().endsWith(".log")) {
					try {
						String key = objectSummary.getKey();
				System.out.println("Key is : " + key);
						LogItem logItem = builder.buildLogItem(s3Api, s3, bucketName, key);
						if(logItem != null) {
							// log.info("Parent directory: {}", file.getParent());
							writer.writeLogItem(logItem, s3Api, s3, bucketName, key);
							String newKey = key + ".DONE";
							s3Api.renameFile(s3, bucketName, key, newKey);
							// moveFile(file);
						}
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		System.out.println();
	}
	private void moveFile(File file) {
		try {
			File newFile = new File(file.getCanonicalFile()+".DONE");
			com.google.common.io.Files.move(file, newFile);
		} catch ( IOException e) {
			log.error("Exception when moving file to .DONE. File={}, Exeption={}",file.getName(), e);
		}
	}
}
