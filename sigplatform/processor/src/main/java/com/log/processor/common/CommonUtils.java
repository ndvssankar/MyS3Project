package com.log.processor.common;

import org.mongojack.JacksonDBCollection;

import com.log.processor.core.LogItem;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class CommonUtils {
	public static final String TEST_LOG = "test_log";
	public static final String TEST_NAME = "test_name";
	public static final String DOT_LOG = ".log";
	public static final String PASSED = "PASSED";
	public static final String FAILED = "FAILED";
	public static final String ERROR = "ERROR";
	public static final String TERMINATED = "TERMINATED";
	public static final String REGRESS = "regress";
	public static final String PCIXP = "pcixp";
	
	public static JacksonDBCollection<LogItem,String> getCollection(DB db) {
		DBCollection dbCol = db.getCollection( TEST_LOG );
		return JacksonDBCollection.wrap(dbCol, LogItem.class, String.class);
	}
}
