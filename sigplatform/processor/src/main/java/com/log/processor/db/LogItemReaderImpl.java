package com.log.processor.db;

import static org.slf4j.LoggerFactory.getLogger;

import org.mongojack.DBQuery;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.log.processor.common.CommonUtils;
import com.log.processor.core.LogItem;
import com.mongodb.DB;
import com.mongodb.MongoException;

public class LogItemReaderImpl implements LogItemReader {
	private static final Logger log = getLogger(LogItemReaderImpl.class);
	private DB db;
	
	@Inject
	public LogItemReaderImpl(DB db) {
		this.db = db;
	}
	@Override
	public LogItem fetchLogItemByKey(String key) {
		try {
			return CommonUtils.getCollection(db).findOne( DBQuery.is(CommonUtils.TEST_NAME, key) );
		} catch ( MongoException e) {
			log.error("Exception while fetching document for the key. Key={}", key);
		}
		return null;
	}

}
