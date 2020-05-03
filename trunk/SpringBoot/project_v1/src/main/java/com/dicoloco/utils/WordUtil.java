package com.dicoloco.utils;

//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.dicoloco.model.Word;

public class WordUtil {
		
	
	/**
	 * Methode correctString : corrige le format d'une chaine de caracteres
	 * @param la chaine a modifier
	 * @return la chaine modifiee
	 */
	public static String correctString(String word) {
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
	 * Methode correctArrayString : corrige le format d'une liste de chaines de caracteres
	 * @param la liste a modifier
	 * @return la liste modifiee
	 */
	public static List<String> correctArrayString(List<String> words){
		List<String> newWords = new ArrayList<>();
		
		for(int i=0; i<words.size();i++) {
			newWords.add(correctString(words.get(i)));
		}
		
		return newWords;
	}
	
	/**
	 * Methode appendDefWithCategory : permet de coller la definition avec la categorie qui correspond
	 * Corrige la synthaxe si necessaire du type et de la definition
	 * Si le mot est anglais, on ne prend pas en compte le genre
	 * @param typeWord la categorie
	 * @param definitions la definition
	 * @return la chaine de caracteres 
	 */
	
	public static String appendDefWithCategory(String typeWord, String definitions, String gender, String language) {
		if(language.equals("en")) {
			String tempType = correctString(typeWord);	
			String tempDef = correctString(definitions);	

			return tempType + " : " + tempDef;
		}
		
		else if(language.equals("fr")) {
			if(gender.equals("")) {
				String tempType = correctString(typeWord);	
				String tempDef = correctString(definitions);	

				return tempType + " : " + tempDef;
			}
			else {
				String tempType = correctString(typeWord);	
				String tempDef = correctString(definitions);	
				String tempGender = correctString(gender);
				return tempType + " "+ tempGender+ " : " + tempDef;
			}
		}
		else {
			String tempType = correctString(typeWord);	
			String tempDef = correctString(definitions);	

			return tempType + " : " + tempDef;
		}
	}
	
	/**
	 * Methode transformListWordJSON : permet de placer les categories à cote des definitions
	 * La BDD possede des mots qui ont le meme nom, donc on recupere 
	 * leur définitions et categories et on les ajoute dans un seul mot
	 * @param wordsJSON la liste de mots a transformer
	 * @return une liste de mots transformee
	 */
	public static List<Word> transformListWordJSON(List<Word> wordsFirst){
		List<Word> words = new ArrayList<>(); 
		
		for(int i=0; i<wordsFirst.size(); i++) {
			
			List<String> definitions = new ArrayList<>();
			List<String> definitionsFirst = wordsFirst.get(i).getDefinitions();
			for(int h=0;h<definitionsFirst.size(); h++) {
				definitions.add(appendDefWithCategory(wordsFirst.get(i).getCategory(), definitionsFirst.get(h), wordsFirst.get(i).getGender(), wordsFirst.get(i).getLanguage()));
			}
			List<String> synonyms = new ArrayList<>();
			List<String> synonymsFirst = wordsFirst.get(i).getSynonyms();
			for(int h=0;h<synonymsFirst.size(); h++) {
				synonyms.add(correctString(synonymsFirst.get(h)));
			}

			String name = correctString(wordsFirst.get(i).getName());
			wordsFirst.get(i).setName(name);
			
			for(int j=i+1; j<wordsFirst.size();j++) {
				String temp = wordsFirst.get(j).getName();
				wordsFirst.get(j).setName(correctString(temp));
				
				if(wordsFirst.get(i).getName().equals(wordsFirst.get(j).getName())) {
							
					String typeOther = wordsFirst.get(j).getCategory();
					List<String> defOther = wordsFirst.get(j).getDefinitions();
						
					for(int k=0; k<defOther.size(); k++) {
						definitions.add(appendDefWithCategory(typeOther, wordsFirst.get(j).getDefinitions().get(k), wordsFirst.get(j).getGender(), wordsFirst.get(j).getLanguage()));
					}
					
					wordsFirst.remove(j);
					j--;
					
				}
			}			
			
			words.add(new Word(wordsFirst.get(i).getName(), definitions, "Genre a cote des definitions", "Categorie a cote des definitions", synonyms , wordsFirst.get(i).getLanguage()));
		}
		
		return words;
		
	}
	
}
