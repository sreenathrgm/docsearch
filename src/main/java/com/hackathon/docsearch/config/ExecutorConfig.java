package com.hackathon.docsearch.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorConfig {

	@Bean(name = "documentWatcherExecutor")
    public Executor threadPoolExecutor() {
        return Executors.newFixedThreadPool(1);
    }
}
