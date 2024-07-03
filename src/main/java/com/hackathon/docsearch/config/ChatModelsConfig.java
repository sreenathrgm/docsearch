package com.hackathon.docsearch.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hackathon.docsearch.utils.ModelNameInOllma;

import dev.langchain4j.model.ollama.OllamaChatModel;

@Configuration
public class ChatModelsConfig {
    
    @Value("${ollama.chat.model.base-url}")
    private String ollamaBaseUrl; 

	@Bean(name = "ollamaModel")
	OllamaChatModel ollamaChatModel() {
		return OllamaChatModel.builder()
				.baseUrl(ollamaBaseUrl)
				.logRequests(true)
				.logResponses(true)
				.modelName(ModelNameInOllma.LLAMA3.getModelName())
				.temperature(0.7)
				// .maxRetries(1)
				.timeout(Duration.ofMinutes(15))
				.build();

	}
	
	

}
