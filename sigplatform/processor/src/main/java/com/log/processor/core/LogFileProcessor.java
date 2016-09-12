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

public class LogFileProcessor extends AbstractLogProcessor {
	private static final Logger log = getLogger(LogFileProcessor.class);
	private String directory;
	private LogItemBuilder builder;
	private LogItemWriter writer;
	
	@Inject
	public LogFileProcessor( @Named("directory") String directory, LogItemBuilder builder, LogItemWriter writer ) {
		this.directory = directory;
		this.builder = builder;
		this.writer = writer;
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
		try {
			File dir = new File(directory);
			FilenameFilter filter = new FilenameFilter() {
			    public boolean accept(File dir, String name) {
			        return name.endsWith(CommonUtils.DOT_LOG);
			    }
			};
			//log.info("Getting all files in " + dir.getCanonicalPath() + " including those in subdirectories");
			List<File> files = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
			for (File file : files) {
				if (file.getName().endsWith(CommonUtils.DOT_LOG)) {
					System.out.println("===========file: ========" + file.getParent());
					LogItem logItem = builder.buildLogItem( file );
					log.info("Parent directory: {}", file.getParent());
					writer.writeLogItem( logItem, file.getParent(), file.getName() );
					moveFile(file);
				}
			}
		} catch(Exception e) {
			log.error("Exception when traversing files. Exeption={}", e);
			e.printStackTrace();
		}
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
