package com.log.processor;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.log.processor.config.LogProcessorConfiguration;
import com.log.processor.core.LogFileProcessor;
import com.log.processor.core.S3LogFileProcessor;
import com.log.processor.core.AbstractLogProcessor;
import com.log.processor.core.LogItemBuilder;
import com.log.processor.core.LogItemBuilderImpl;
import com.log.processor.core.LogProcessorManager;
import com.log.processor.db.DBImpl;
import com.log.processor.db.LogItemReader;
import com.log.processor.db.LogItemReaderImpl;
import com.log.processor.db.LogItemWriter;
import com.log.processor.db.LogItemWriterImpl;
import com.log.processor.db.MongoClientImpl;
import com.log.processor.db.MongoClientURIImpl;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogProcessorModule extends AbstractModule {
	private static final Logger log = getLogger(LogProcessorApplication.class);
	private LogProcessorConfiguration configuration;
	private Environment environment;

	public LogProcessorModule(LogProcessorConfiguration c, Environment e) {
		this.configuration = c;
		this.environment = e;
	}

	@Override
	protected void configure() {
		bind(String.class).annotatedWith(Names.named("directory"))
			.toInstance(configuration.getFileConfiguration().getDirectory());
		bind(String.class).annotatedWith(Names.named("bucket"))
			.toInstance(configuration.getS3Configuration().getBucket());
		bind(Integer.class).annotatedWith(Names.named("numJobs"))
	    	.toInstance(configuration.getJobConfiguration().getNumJobs());
		bind(String.class).annotatedWith(Names.named("url"))
			.toInstance(configuration.getMongoConfiguration().getUrl());
		bind(MongoClient.class).toProvider(MongoClientImpl.class);
		bind(MongoClientURI.class).toProvider(MongoClientURIImpl.class);
		bind(DB.class).toProvider(DBImpl.class);
		bind(LogProcessorConfiguration.class).toInstance(configuration);
		bind(Environment.class).toInstance(environment);
		bind(LogItemReader.class).to(LogItemReaderImpl.class);
		bind(LogItemWriter.class).to(LogItemWriterImpl.class);
		bind(LogItemBuilder.class).to(LogItemBuilderImpl.class);
		bind(AbstractLogProcessor.class).to(LogFileProcessor.class);
	}

	@Provides
	@Inject
	private ExecutorService buildExecutor() {
		return Executors.newCachedThreadPool();
	}

	@Provides
	@Inject
	private List<AbstractLogProcessor> buildLogFileProcessors(
			@Named("numJobs") int numJobs, Injector injector ) {

		List<AbstractLogProcessor> readers = new ArrayList<>();
		if(!(configuration.getFileConfiguration().getDirectory().equals("None"))) {
			for (int job=0; job < numJobs; job++) {
				readers.add( injector.getInstance(LogFileProcessor.class) );
			}
		} else {
			readers.add( injector.getInstance(S3LogFileProcessor.class));
		}
		return readers;
	}

	@Provides
	@Singleton
	@Inject
	private LogProcessorManager buildLogProcessorManager(ExecutorService executor, List<AbstractLogProcessor> readers) {
		try {
			return new LogProcessorManager(executor, readers);
		} catch (Exception e) {
			log.error("Could not build LogProcessorManager");
			throw new RuntimeException(e);
		}
	}
}
