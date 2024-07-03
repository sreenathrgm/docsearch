package com.hackathon.docsearch.service;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.hackathon.docsearch.utils.DocumentWatcher;

@Service
public class DocumentWatcherService {
	
	public Executor threadPoolExecutor;
	
    private DocumentWatcher documentWatcher;

    public DocumentWatcherService(DocumentWatcher documentWatcher, @Qualifier("documentWatcherExecutor") Executor executor) {
        this.threadPoolExecutor = executor;
		this.documentWatcher = documentWatcher;
    }

    @Scheduled(fixedRate = 120000)
    public void startWatcher() {
    	//threadPoolExecutor.execute(documentWatcher::startWatching);
    	documentWatcher.startWatching();
    }
}

