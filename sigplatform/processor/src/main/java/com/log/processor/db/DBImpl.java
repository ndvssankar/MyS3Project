package com.log.processor.db;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class DBImpl implements Provider<DB> {
	private final MongoClientURI uri;
	private final MongoClient client;
	
	@Inject
	public DBImpl(MongoClientURI uri, MongoClient client) {
		this.uri = uri;
		this.client = client;
	}
	@Override
	public synchronized DB get() {
		return client.getDB(uri.getDatabase());
	}

}
