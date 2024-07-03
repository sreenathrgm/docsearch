package com.hackathon.docsearch.utils;

public enum ModelNameInOllma {

	CODELLAMA("codellama:7b"), 
	MISTRAL("mistral:7b"), 
	MISTRAL_INSTRUCT("mistral:7b-instruct"), 
	LLAMA3("llama3");

	private final String modelName;

	ModelNameInOllma(String modelName) {
		this.modelName = modelName;
	}

	public String getModelName() {
		return modelName;
	}

}
