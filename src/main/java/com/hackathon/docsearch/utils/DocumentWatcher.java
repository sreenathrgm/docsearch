package com.hackathon.docsearch.utils;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocumentsRecursively;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.HuggingFaceTokenizer;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DocumentWatcher {

	@Autowired
	private EmbeddingModel embeddingModel;
	
	@Autowired
	private EmbeddingStore<TextSegment> embeddingStore;
	
    private final Path pathToWatch;

    public DocumentWatcher(@Value("${docs.location}") String directoryPath) {
        this.pathToWatch = Paths.get(directoryPath);
    }

    public void startWatching() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            pathToWatch.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                WatchKey key;
                try {
                    key = watchService.take();
                } catch (InterruptedException e) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }

                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();
                    log.info("File {}: {} ", kind.name(), fileName);
                    
                    // Load the document using langchain4j
                    loadDocument(pathToWatch.resolve(fileName).toString());
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (IOException e) {
        	log.error("IOException occured while document watching due to error {} , errorTrace={}" , e.getMessage(), e);
        }
    }

    private void loadDocument(String filePath) {
    	log.info("Loading document from path : " + filePath);
    	try {
    		var documents = loadDocumentsRecursively(filePath, new ApacheTikaDocumentParser());
    		DocumentSplitter documentSplitter = DocumentSplitters.recursive(500, 0, new HuggingFaceTokenizer());
    		EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
    				.documentSplitter(documentSplitter)
    				.embeddingModel(embeddingModel)
    				.embeddingStore(embeddingStore)
    				.build();
    		ingestor.ingest(documents);
    		documents.forEach(DocumentWatcher::log);
    		log.info("Imported {} documents", documents.size());
        } catch (Exception e) {
        	log.error("Exception occured while loading the docuements  due to error {} , errorTrace={}" , e.getMessage(), e);
        }   	
        
    }    
    
    private static void log(Document document) {
		log.info("fileName={}, path={} ", document.metadata().getString("file_name"), document.metadata().getString("absolute_directory_path"));
	}
    
}

