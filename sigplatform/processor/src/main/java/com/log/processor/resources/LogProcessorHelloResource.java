package com.log.processor.resources;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.log.processor.core.LogItem;
import com.log.processor.core.Saying;
import com.log.processor.db.LogItemReader;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;

@Path("/hellosigtrak")
@Produces(MediaType.APPLICATION_JSON)
public class LogProcessorHelloResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    @Inject
    public LogProcessorHelloResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }
    
    @GET
    public Saying sayHello(@QueryParam("name") Optional<String> name) {
        return new Saying(counter.incrementAndGet(),
                          String.format(template, name.or(defaultName)));
    }
}