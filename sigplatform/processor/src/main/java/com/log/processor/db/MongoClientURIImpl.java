package com.log.processor.db;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.mongodb.MongoClientURI;

public class MongoClientURIImpl implements Provider<MongoClientURI> {
	private final String url;
	
	@Inject
	public MongoClientURIImpl(@Named("url") String url) {
		this.url = url;
	}
	@Override
	public MongoClientURI get() {
		return new MongoClientURI( url );
	}

}
