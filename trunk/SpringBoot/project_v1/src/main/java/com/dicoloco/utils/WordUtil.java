package com.dicoloco.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WordUtil {
	
	private static WordUtil INSTANCE = null;
	
	private WordUtil() {
		
	}
	
	public static WordUtil getInstance() {
		
		if (INSTANCE == null) {
			INSTANCE = new WordUtil();
		}
		
	    return INSTANCE;
	}

	/**
	 * Corrige le format d'une chaine de caracteres
	 * @param la chaine a modifier
	 * @return la chaine modifiee
	 */
	public String correctString(String word) {
		String firstLetter = word.trim();
		if(firstLetter.length()<2) {
			word = firstLetter.substring(0, 1).toUpperCase();
		}
		else
			word = firstLetter.substring(0, 1).toUpperCase() + firstLetter.substring(1).toLowerCase();
		if(word.contains("'")) {
			String temp = word.replaceAll("'", "''");
			word = temp;
		}
		if(word.contains("/")) {
		    String temp = word.replace("\\", " ");
		    word = temp;
		}		
		return word;
		
	}
	
	/**
	 * Corrige le format d'une liste de chaines de caracteres
	 * @param la liste a modifier
	 * @return la liste modifiee
	 */
	public List<String> correctArrayString(List<String> words){
		List<String> newWords = new ArrayList<>();
		
		for(int i=0; i<words.size();i++) {
			newWords.add(this.correctString(words.get(i)));
		}
		
		return newWords;
	}
	
	public String readFromInputStream(InputStream inputStream)
			  throws IOException {
			    StringBuilder resultStringBuilder = new StringBuilder();
			    try (BufferedReader br
			      = new BufferedReader(new InputStreamReader(inputStream))) {
			        String line;
			        while ((line = br.readLine()) != null) {
			            resultStringBuilder.append(line).append("\n");
			        }
			    }
			  return resultStringBuilder.toString();
			}
}
