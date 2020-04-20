package com.dicoloco.model;

import java.util.List;

import javax.persistence.Id;

import org.springframework.data.relational.core.mapping.Column;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown=true)
public class Word {
	
	@Id
	@JsonProperty("name")
	private String name;
	
	@Column
	@JsonProperty("definitions")
	private List<String> definitions;
	
	@Column
	@JsonProperty("gender")
	private String gender;
	
	@Column
	@JsonProperty("category")
	private String category;
	
	@Column
	@JsonProperty("synonyms")
	private List<String> synonyms;
	
	@Column
	@JsonProperty("language")
	private String language;
	
	
	public Word() {
		
	}
	
	@JsonCreator
	public Word(@JsonProperty("name")String name, @JsonProperty("definitions")List<String> definitions, @JsonProperty("gender")String gender, 
			@JsonProperty("category")String category, @JsonProperty("synonyms")List<String> synonyms, @JsonProperty("language")String language) {
		this.name = name;
		this.definitions = definitions;
		this.gender = gender;
		this.category = category;
		this.synonyms = synonyms;
		this.language = language;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getDefinitions() {
		return definitions;
	}

	public void setDefinitions(List<String> definitions) {
		this.definitions = definitions;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCategory() {
		return category;
	}

	public void setCatogory(String category) {
		this.category = category;
	}

	public List<String> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(List<String> synonyms) {
		this.synonyms = synonyms;
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append(name);
		result.append(";");
		for(int i=0; i<definitions.size(); i++) {
			result.append(definitions.get(i));
			result.append("_");
		}
		result.append(category);
		result.append(";");
		result.append(gender);
		result.append(";");
		for(int i=0; i<synonyms.size(); i++) {
			result.append(synonyms.get(i));
			result.append("_");
		}
		result.append(";");				
		result.append(language);
		result.append(";");
		
		return result.toString();
	}
	
}
