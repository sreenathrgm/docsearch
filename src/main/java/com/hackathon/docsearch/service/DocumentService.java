package com.hackathon.docsearch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hackathon.docsearch.utils.Assistant;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;

@Service
public class DocumentService {
    
    @Autowired
    private EmbeddingStore<TextSegment> embeddingStore;
    
    @Autowired
    private ChatMemoryProvider chatMemoryProvider;
    
    @Autowired
    private OllamaChatModel ollamaChatModel;
 
	
	public String docSearch(String query) {	
		
		Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(ollamaChatModel)
                .chatMemoryProvider(chatMemoryProvider)
                .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                .build();
		
		 return assistant.answer(query);
		
	}
	

}
