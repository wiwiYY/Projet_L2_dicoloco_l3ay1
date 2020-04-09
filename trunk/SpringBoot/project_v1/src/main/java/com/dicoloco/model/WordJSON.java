package com.dicoloco.model;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;
/*
 * Classe WordJSON pour le model de la bdd trouvée
 */
public class WordJSON {
	
	@Id
	@JsonProperty("word")
	String word;
	@Id
	@JsonProperty("wordtype")
	String wordtype;
	@Id
	@JsonProperty("definition")
	String definition;
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getWordtype() {
		return wordtype;
	}
	public void setWordtype(String wordtype) {
		this.wordtype = wordtype;
	}
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
}
