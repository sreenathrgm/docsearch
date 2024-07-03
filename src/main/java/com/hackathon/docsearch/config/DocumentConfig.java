package com.hackathon.docsearch.config;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocumentsRecursively;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.HuggingFaceTokenizer;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DocumentConfig {

	@Value("${docs.location}")
	private String docsLocation;

	@Bean
	EmbeddingModel embeddingModel() {
		return new AllMiniLmL6V2EmbeddingModel();
	}

	@Bean
	ChatMemoryProvider chatMemoryProvider() {
		return chatId -> MessageWindowChatMemory.withMaxMessages(10);
	}

	@Bean
	ContentRetriever contentRetriever(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {
		int maxResults = 1;
		double minScore = 0.6;

		return EmbeddingStoreContentRetriever.builder()
				.embeddingStore(embeddingStore)
				.embeddingModel(embeddingModel)
				.maxResults(maxResults)
				.minScore(minScore)
				.build();
	}

	@Bean
	EmbeddingStore<TextSegment> embeddingStore(EmbeddingModel embeddingModel) throws IOException {
		EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
		long startTime = System.currentTimeMillis();
		var documents = loadDocumentsRecursively(docsLocation, new ApacheTikaDocumentParser());

		DocumentSplitter documentSplitter = DocumentSplitters.recursive(500, 0, new HuggingFaceTokenizer());
		EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
				.documentSplitter(documentSplitter)
				.embeddingModel(embeddingModel)
				.embeddingStore(embeddingStore)
				.build();
		ingestor.ingest(documents);
		
		long endTime = System.currentTimeMillis();
		
		log.info("Documents load time {} ms", (endTime - startTime));
		
		documents.forEach(DocumentConfig::log);
		log.info("Imported {} documents", documents.size());
		
		return embeddingStore;
	}	

	private static void log(Document document) {
		log.info("fileName={}, path={} ", document.metadata().getString("file_name"), document.metadata().getString("absolute_directory_path"));
	}

}
