package com.hackathon.docsearch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.docsearch.service.DocumentService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/document")
@EnableAspectJAutoProxy
@Slf4j
public class DocSearchController {
	
	@Autowired
	private DocumentService documentService;
	
	
    @PostMapping(value="/prompt",  consumes={MediaType.ALL_VALUE},   produces={MediaType.ALL_VALUE })
	public ResponseEntity<String> documentAssist(@RequestBody String userQuery) {
		try {
			return ResponseEntity.ok(documentService.docSearch(userQuery));
		}catch (RuntimeException ex) {
			log.error("Failed to process ", ex);
			return ResponseEntity.internalServerError().body("Sorry, can't process your question right now. Due to " +ex.getMessage());
		} 
		catch (Exception e) {
			log.error("Failed to process ", e);
			return ResponseEntity.internalServerError().body("Sorry, can't process your question right now.");
		}
	}

}
