package com.log.processor.db;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.mongojack.JacksonDBCollection;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.log.processor.common.CommonUtils;
import com.log.processor.core.LogItem;
import com.mongodb.DB;
import com.mongodb.MongoException;

import com.amazonaws.services.s3.AmazonS3;
import com.log.processor.core.S3Api;

public class LogItemWriterImpl implements LogItemWriter {
	private static final Logger log = getLogger(LogItemWriterImpl.class);
	private LogItemReader reader;
	private DB db;

	@Inject
	public LogItemWriterImpl( DB db, LogItemReader reader ) {
		this.db = db;
		this.reader = reader;
	}

	@Override
	public void writeLogItem(LogItem item, String directory, String name) {
		JacksonDBCollection<LogItem,String> collection = CommonUtils.getCollection(db);
		// insert(collection, item);
		createFileUsingJackson(item, directory, name);
	}

	@Override
	public void writeLogItem(LogItem item, S3Api s3Api, AmazonS3 s3, String bucketName, String key) {
		JacksonDBCollection<LogItem,String> collection = CommonUtils.getCollection(db);
		// insert(collection, item);
		createFileUsingJackson(item, s3Api, s3, bucketName, key);
	}

	public void createFileUsingGson(LogItem item, String directory, String name) {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		String json = gson.toJson(item);
		if (name.indexOf(".") > 0) {
		    name = name.substring(0, name.lastIndexOf("."));
			name = name + ".json";
		} else {
			return;
		}
		try {
			FileWriter writer = new FileWriter(directory+"/"+name);
			writer.write(json);
			writer.close();
		} catch( IOException e ) {
			log.error("Gson Error writing json file for the item={}", e);
		}
	}

	public void createFileUsingJackson(LogItem item, S3Api s3Api, AmazonS3 s3, String bucketName, String key) {
		ObjectMapper mapper = new ObjectMapper();
		key = key.substring(0, key.lastIndexOf("."));
		if (key.indexOf(".") > 0) {
			key = key + ".json";
		} else {
			return;
		}
		try {
			String jsonInString = mapper.writeValueAsString(item);
			s3Api.uploadObject(s3, bucketName, key, jsonInString);
		} catch (Exception e) {
			log.error("Jackson Error writing json file for the item={}", e);
		}
	}

	public void createFileUsingJackson(LogItem item, String directory, String name) {
		ObjectMapper mapper = new ObjectMapper();
		if (name.indexOf(".") > 0) {
		    name = name.substring(0, name.lastIndexOf("."));
		    name = name + ".json";
		} else {
			return;
		}
		try {
			String fileName = directory+"/"+name;
			System.out.println("File Name is : " + fileName);
			log.info("FileName {}", fileName);
			mapper.writeValue(new File(fileName), item);
			//String jsonInString = mapper.writeValueAsString(item);
		} catch (Exception e) {
			log.error("Jackson Error writing json file for the item={}", e);
		}
	}
	
	public void insert(JacksonDBCollection<LogItem,String> collection, LogItem item) {
		try {
			collection.insert( item );
		} catch( MongoException e) {
			log.error("Exceptiin when inserting the item. Item={}. Exception={}", item,e);
		}
	}
	
	public void update(JacksonDBCollection<LogItem,String> collection, LogItem item, String id) {
		try {
			collection.updateById( id, item );
		} catch( MongoException e) {
			log.error("Exceptiin when inserting the item. Item={}. Exception={}", item,e);
		}
	}

}
