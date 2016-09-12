package com.log.processor;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.log.processor.config.LogProcessorConfiguration;
import com.log.processor.core.LogFileProcessor;
import com.log.processor.core.LogProcessorManager;
import com.log.processor.health.TemplateHealthCheck;
import com.log.processor.resources.LogProcessorHelloResource;
import com.log.processor.resources.LogProcessorResource;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;

import static org.slf4j.LoggerFactory.getLogger;
import org.slf4j.Logger;

@Slf4j
public class LogProcessorApplication extends Application<LogProcessorConfiguration> {
	private static final Logger log = getLogger(LogProcessorApplication.class);
	private Injector injector;
	
    public static void main(final String[] args) throws Exception {
        new LogProcessorApplication().run(args);
    }

    @Override
    public String getName() {
        return "Hello-SigTrak";
    }

    @Override
    public void initialize(final Bootstrap<LogProcessorConfiguration> bootstrap) {
    	bootstrap.setConfigurationSourceProvider(
    			new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false))
    			);
    }

    @Override
    public void run(final LogProcessorConfiguration configuration,
                    final Environment environment) {
    	log.info("Entered LogProcessorApplication");
    	
    	this.injector = Guice.createInjector(new LogProcessorModule(configuration, environment));
    	final LogProcessorHelloResource resource = new LogProcessorHelloResource(
    	        configuration.getTemplate(),
    	        configuration.getDefaultName()
    	    );
    	final TemplateHealthCheck healthCheck =
    	        new TemplateHealthCheck(configuration.getTemplate());
    	environment.healthChecks().register("template", healthCheck);
    	environment.jersey().register(resource);
    	environment.jersey().register(injector.getInstance(LogProcessorResource.class));
    	environment.lifecycle().manage(injector.getInstance(LogProcessorManager.class));
    }
}
