package com.hackathon.docsearch.utils;

import lombok.Getter;

public enum HuggingFaceModelName {
	
	SENTENCE_TRANSFORMERS_ALL_MINI_LM_L6_V2("sentence-transformers/all-MiniLM-L6-v2"),
	TII_UAE_FALCON_7B_INSTRUCT("tiiuae/falcon-7b-instruct");
    
    @Getter
    private String name;

    private HuggingFaceModelName(String name) {
         this.name = name;
    }

}
