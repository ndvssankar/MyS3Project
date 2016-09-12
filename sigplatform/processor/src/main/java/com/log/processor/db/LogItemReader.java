package com.log.processor.db;

import com.log.processor.core.LogItem;

public interface LogItemReader {

	public LogItem fetchLogItemByKey( String key );
}
