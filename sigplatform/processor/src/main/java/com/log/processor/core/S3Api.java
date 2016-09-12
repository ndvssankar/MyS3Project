package com.log.processor.core;

import java.util.*;
import java.io.File;
import java.io.Writer;
import java.util.UUID;
import java.util.List;
import java.util.Date;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.stream.Collectors;

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

public class S3Api {

	private AmazonS3 s3;

	public S3Api() {
		s3 = new AmazonS3Client();
        	Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        	s3.setRegion(usWest2);

	        System.out.println("===========================================");
        	System.out.println("Getting Started with Amazon S3");
	        System.out.println("===========================================\n");
	}

	public AmazonS3 getConnection() {
		return s3;
	}

	public boolean createBucket(String bucketName, AmazonS3 s3) {
		try {
			s3.createBucket(bucketName);
			return true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public List<Bucket> getBucketList(AmazonS3 s3) {
		try {
			System.out.println("gettings buckets...");
			return s3.listBuckets();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public boolean uploadObject(AmazonS3 s3, String bucketName, String key, String jsonString) {
		try {
			File file = File.createTempFile("One"+UUID.randomUUID(), ".json");
			file.deleteOnExit();
			Writer writer = new OutputStreamWriter(new FileOutputStream(file));
			writer.write(jsonString);
			writer.close();
			s3.putObject(new PutObjectRequest(bucketName, key, file));
			return true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public Date getLastModified(AmazonS3 s3, String bucketName, String key) {
		return (s3.getObjectMetadata(bucketName, key)).getLastModified();
	}

	public List<String> downloadObject(AmazonS3 s3, String bucketName, String key) throws IOException {
		S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
		return displayTextInputStream(object.getObjectContent());
	}

	public List<S3ObjectSummary> getObjectsFromBucket(AmazonS3 s3, String bucketName) {
		try {
			ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
				.withBucketName(bucketName));
			return objectListing.getObjectSummaries();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public boolean renameFile(AmazonS3 s3, String bucketName, String key, String newKey) {
		try {
			CopyObjectRequest copyObjRequest = new CopyObjectRequest(bucketName, key, bucketName, newKey);
			s3.copyObject(copyObjRequest);
			s3.deleteObject(new DeleteObjectRequest(bucketName, key));
			return true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public boolean deleteObject(AmazonS3 s3, String bucketName, String key) {
		try {
			s3.deleteObject(bucketName, key);
			return true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public boolean deleteBucket(AmazonS3 s3, String bucketName) {
		try {
			s3.deleteBucket(bucketName);
			return true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	private static List<String> displayTextInputStream(InputStream input) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));

		List<String> list = new ArrayList<String>();
		list = reader.lines().collect(Collectors.toList());
		return list;
	}

	public String getBucketName(String objectSummary) {
      		return objectSummary.substring(0,objectSummary.lastIndexOf("/"));
   	}

	public String getKey(String objectSummary) {
      		return objectSummary.substring(objectSummary.lastIndexOf("/")+1);
   	}
}
