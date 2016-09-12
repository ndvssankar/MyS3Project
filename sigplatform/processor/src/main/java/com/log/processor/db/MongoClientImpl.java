package com.log.processor.db;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoClientImpl implements Provider<MongoClient> {
	private MongoClient client;
	private MongoClientURI uri;
	
	@Inject
	public MongoClientImpl(MongoClientURI uri) {
		this.uri = uri;
	}
	@Override
	public synchronized MongoClient get() {
		if (client == null) {
			this.client = new MongoClient(uri);
		}
		return client;
	}

}
