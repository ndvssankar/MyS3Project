package com.log.processor.resources;

import com.google.inject.Inject;
import com.log.processor.core.LogItem;
import com.log.processor.db.LogItemReader;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/sigtrak/1")
@Produces(MediaType.APPLICATION_JSON)
public class LogProcessorResource {
    private LogItemReader reader;

    @Inject
    public LogProcessorResource(LogItemReader reader) {
        this.reader = reader;
    }
    
    @POST 
    @Path("/log/data/read.json")
    public LogItem readLog(@QueryParam("name") String name){
    	LogItem item = reader.fetchLogItemByKey(name);
    	return item;
    }
}