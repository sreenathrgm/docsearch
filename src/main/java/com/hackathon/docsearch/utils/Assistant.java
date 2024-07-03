package com.hackathon.docsearch.utils;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService(chatModel = "ollamaChatModel")
public interface Assistant {

	@SystemMessage("AI Assitant- ")
    String answer(@UserMessage String query);
}
