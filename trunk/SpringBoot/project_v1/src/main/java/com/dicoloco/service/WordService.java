package com.dicoloco.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dicoloco.dao.WordDao;
import com.dicoloco.model.Word;
import com.dicoloco.model.WordAPI;
import com.dicoloco.utils.WordUtil;

@Service
public class WordService {
	
	private WordDao wordDao = new WordDao();
	
	@Autowired
	public WordService(@Qualifier("dao")WordDao wordDao) {
		this.wordDao = wordDao;
	}
	
	public WordService() {
		
	}
	
	
	/**
	 * Methode findWordByNameLanguage : cherche et retourne le mot recherche selon la langue 
	 * @param name le nom du mot
	 * @param language la langue du mot
	 * @return le mot recherche ou null si pas trouve
	 */
	public Word findWordByNameLanguage(String name, String language){
		String nameUpper = name;
		Word found = wordDao.findWord(nameUpper, language);
		List<String> definitions = new ArrayList<>();
		
		if(found != null) {
			for(int i=0; i<found.getDefinitions().size(); i++) {
				StringBuffer defBuffer = new StringBuffer();
				
				String definitionFound = found.getDefinitions().get(i);
				definitionFound = definitionFound.replace("\n", "");
				
				defBuffer.append(definitionFound);
				defBuffer.append("_");
				
				definitions.add(i, defBuffer.toString());
			}
			found.setDefinitions(definitions);
						
			List<Word> words = wordDao.findAllWordsWithLanguage(language);
			List<String> synonyms = new ArrayList<>();
			for(int i=0; i<found.getSynonyms().size();i++) {
				boolean synonymFound = false;
				for(int j=0; j<words.size();j++) {
					if(words.get(j).getName().equals(found.getSynonyms().get(i))) {
						synonymFound = true;
					}
				}
				if(synonymFound) {
					synonyms.add(found.getSynonyms().get(i));
				}
			}
			
			found.setSynonyms(synonyms);
			StringBuffer synBuff = new StringBuffer();
			for(int i=0; i<synonyms.size();i++) {
				synBuff.append(synonyms.get(i));
				synBuff.append("_");
			}
			wordDao.updateWord(found.getName(), synBuff.toString(), language);
			return found;
		}
		return null;
	}
	
	/**
	 * Methode findWordByNameLanguage : Cherche et retourne le mot recherche selon la langue 
	 * Methode redefinie : elle contient la liste de tous les noms de la bdd qui sont de la meme
	 * langue que le mot recherche. Cela permet d'eviter de repeter des appels vers la bdd
	 * @param name
	 * @param language
	 * @param liste des mots
	 * @return le mot recherche ou null si pas trouve
	 */
	public Word findWordByNameLanguage(String name, String language, List<String> names){
		Word found = wordDao.findWord(name, language);
		List<String> definitions = new ArrayList<>();
		
		if(found != null) {
			for(int i=0; i<found.getDefinitions().size(); i++) {
				StringBuffer defBuffer = new StringBuffer();
				
				String definitionFound = found.getDefinitions().get(i);
				definitionFound = definitionFound.replace("\n", "");
				
				defBuffer.append(definitionFound);
				defBuffer.append("_");
				
				definitions.add(i, defBuffer.toString());
			}
			found.setDefinitions(definitions);
						
			List<String> synonyms = new ArrayList<>();
			for(int i=0; i<found.getSynonyms().size();i++) {
				boolean synonymFound = false;
				for(int j=0; j<names.size();j++) {
					if(names.get(j).equals(found.getSynonyms().get(i))) {
						synonymFound = true;
					}
				}
				if(synonymFound) {
					synonyms.add(found.getSynonyms().get(i));
				}
			}
			
			found.setSynonyms(synonyms);
			StringBuffer synBuff = new StringBuffer();
			for(int i=0; i<synonyms.size();i++) {
				synBuff.append(synonyms.get(i));
				synBuff.append("_");
			}
			wordDao.updateWord(found.getName(), synBuff.toString(), language);
			return found;
		}
		return null;
	}
	
	/**
	 * Methode findSuggestionByName : cherche et retourne les suggestions du mot recherche
	 * D'abord en remplaçant toutes les lettres une par une par toutes les lettres de l'alphabet
	 * Puis en regardant dans la liste de mots la bdd si un mot commence ou contient ce mot 
	 * @param name le mot tape
	 * @param language la langue du mot tape
	 * @return liste des suggestions du mot recherche ou null si pas trouve
	 */
	public List<Word> findSuggestionByName(String name, String language){
		String nameUpper = (WordUtil.correctString(name)).toLowerCase();
		String partNameNotChangePre = null;
		String partNameNotChangePost = null; 
		String partNameChange = null;
		String buff = null;
		
		final int TAILLE_TAB = 10;
		List<Word> listSugg = new ArrayList<Word>();
		Word word = null; 
		
		List<Word> bdd = wordDao.findAllWordsWithLanguage(language);
		
		for(int j = 1; j<=nameUpper.length(); j++) {	
			for(int i = 97; i<=122 ; i++) {
				partNameNotChangePre = nameUpper.substring(0, j-1);
				partNameNotChangePost = nameUpper.substring(j, nameUpper.length());				
				partNameChange = Character.toString((char)i);
				buff = (partNameNotChangePre.concat(partNameChange.concat(partNameNotChangePost)));
				
				for(int k=0; k<bdd.size(); k++) {
					if(buff.equals(bdd.get(k).getName()) && bdd.get(k).getLanguage().equals(language)) {
						word = bdd.get(k);
						if(listSugg.size()<TAILLE_TAB) {
							listSugg.add(word);
						}
					}
				}
			}
		}
		
		for(int l = 0; l<bdd.size(); l++) {
			if(bdd.get(l).getName().startsWith(name) || bdd.get(l).getName().startsWith(nameUpper) || bdd.get(l).getName().contains(name)) {
				word = bdd.get(l);
				if(listSugg.size()<TAILLE_TAB) {
					listSugg.add(word);
				}
			}
		}
		return listSugg;
	}
	
	/**
	 * Methode findAllWord : retourne la liste des mots de la bdd
	 * @return la liste des mots de la bdd
	 */
	public List<Word> findAllWord(){	
		return wordDao.findAllWords();
	}
	
	/**
	 * Methode findAllWordWithLanguage : retourne la liste des mots de la bdd qui sont de la langue indiquee
	 * @param language la langue recherchee
	 * @return la liste des mots de la bdd 
	 */
	public List<Word> findAllWordWithLanguage(String language){
		return wordDao.findAllWordsWithLanguage("en");
	}
	
	/**
	 * Methode updateWord : met a jour la liste de synonymes d'un mot
	 * @param wordName le mot concerne
	 * @param wordSynonym le synonyme a ajouter ou supprimer
	 * @param language la langue du mot
	 * @param method ajouter ou supprimer
	 * @return le resultat : si 1, succes, si 2, le mot entre n'appartient pas a la bdd, si 3, pour delete, synonyme pas trouve dans la liste de ces synonymes,
	 * si 4, pour ajout, le synonyme fait deja parti de la liste de synonymes du mot, si 5, pour ajout, le synonyme n'existe pas dans la bdd
	 */
	public int updateWord(String wordName, String wordSynonym, String language, String method) {
		wordSynonym = WordUtil.correctString(wordSynonym);
		Word word = wordDao.findWord(wordName, language);
		Word wordSyn = wordDao.findWord(wordSynonym, language);
		
		if(wordSyn== null) { 
			return 2;
		}
		else {
			List<String> synonyms = word.getSynonyms();
			if(method.equals("add")) { 
				
				if(synonyms.contains(wordSynonym)) { 
					return 4;
				}
				else {
					synonyms.add(wordSynonym);
				}
			}
			else if(method.equals("delete")){
				if(!synonyms.contains(wordSynonym)) {
					return 3;
				}
				else {
					for(int i=0; i<synonyms.size(); i++) {
						if(synonyms.get(i).equals(wordSynonym)){
							synonyms.remove(i);
						}
					}
				}
			}
			else
				//Erreur methode
				return 5;
			
			StringBuffer synonymsBuffer = new StringBuffer();
			for(int i=0; i<synonyms.size(); i++) {
				synonymsBuffer.append(synonyms.get(i));
				synonymsBuffer.append("_");
			}
			wordDao.updateWord(wordName, synonymsBuffer.toString(), language);
			//System.out.println("Mise à jour des synonymes de "+wordName+" avec "+synonymsBuffer);
			return 1;
		}
	}
	
	/**
	 * Methode deleteWordService : supprime un mot de la bdd
	 * @param name le nom du mot a supprimer
	 * @return 0 si bien supprime, 1 si pas supprime, 2 si le mot n'existe pas
	 */
	public int deleteWordService(String name, String language) {
		name = WordUtil.correctString(name);
		
		if(wordDao.findWord(name, language) != null) {
			return wordDao.deleteWord(name, language);
		}
		
		return 2;
	}
	
	/**
	 * Methode createWordService :creer un nouveau mot dans la bdd
	 * @param name le nom du mot 
	 * @param definitions les definitions du mot
	 * @param gender le genre du mot
	 * @param category la categorie du mot
	 * @param synonyms les synonymes du mot
	 * @param language la langue du mot
	 * @return 0 si le mot a bien ete ajoute, 1 si le mot n'a pas ete ajoute, 2 si le mot existe deja
	 */
	public int createWordService(String name, String definitions, String gender, String category, String synonyms, String language) {
		String nameUpper = WordUtil.correctString(name);
		if(wordDao.findWord(nameUpper, language) == null) {
			if(definitions.equals("_") && !synonyms.equals("_")) {
				wordDao.createWord(nameUpper, "", gender, category, synonyms, language);
			}
			else if(!definitions.equals("_") && synonyms.equals("_")) {
				wordDao.createWord(nameUpper, definitions, gender, category, "", language);
			}
			else if(definitions.equals("_") && synonyms.equals("_")) {
				wordDao.createWord(nameUpper, "", gender, category, "", language);
			}
			else {
				wordDao.createWord(nameUpper, definitions, gender, category, synonyms, language);
			}
		} else {
			return 2;
		}
		
		if(wordDao.findWord(nameUpper, language) == null) {
			return 1;
		} else {
			return 0;
		}
	}
	
	/**
	 * Methode createListWordService : ajout d'une liste de mots
	 * @param words la liste des mots
	 * @return 0 l'ajout s'est bien passe, 1 si erreur sql, 2 si le mot est deja dans la bdd
	 */
	public int createListWordService(List<Word> words) {
		List<Word> wordsDB = wordDao.findAllWords();
		boolean singleWord = false;
		if(words.size()==1) {
			singleWord = true;
		}
		
		for(int i=0; i<words.size();i++) {
			String wordName = words.get(i).getName();
			String wordLanguage = words.get(i).getLanguage();
			
			for(int j=0; j<wordsDB.size();j++) {
				String wordNameDB = wordsDB.get(j).getName();
				String wordLanguageDB = wordsDB.get(j).getLanguage();
				if(wordNameDB.equals(wordName) && wordLanguageDB.equals(wordLanguage)) {		
			    	words.remove(i);
			    	i--;
			    	j=wordsDB.size();
				}
			}
		}
		
		for(int i=0; i<words.size();i++) {
			List<String> definitions = new ArrayList<>();
			for(int k=0;k<words.get(i).getDefinitions().size();k++) {
				String def = words.get(i).getDefinitions().get(k).replaceAll("'", "''");
				definitions.add(def);
			}
			words.get(i).setDefinitions(definitions);
			List<String> synonyms = words.get(i).getSynonyms();
			for(int j=0; j<synonyms.size();j++) {
				Boolean found = false;
				for(int k=0; k<wordsDB.size();k++) {
					if(wordsDB.get(k).getName().equals(synonyms.get(j))){
						found = true;
						k=wordsDB.size();
					}
				}
				if(found==false) {
					synonyms.remove(j);
					j--;
				}
			}
			for(int h=0; h<synonyms.size();h++) {
				String syn = synonyms.get(h);
				
				for(int l=h+1;l<synonyms.size();l++) {
					
					if(syn.equals(synonyms.get(l))){
						synonyms.remove(l);
						l--;
					}
				}
			}
			words.get(i).setSynonyms(synonyms);
		}
		
		if(singleWord && words.size()==0) {
			return 2;
		}
		return wordDao.createWordBDDFound(words);
	}
	
	/**
	 * Methode getRandomWord : recupere une liste de mots aleatoires 
	 * @return la liste de mots aleatoires
	 */
	public List<Word> getRandomWord() {
		List<Word> listAllWords = findAllWordWithLanguage("en");
		List<Word> listRandomWords = new ArrayList<>();
		int a = 0;
		int b = 0;
		int c = 0;
		Word wordA = null;
		Word wordB = null;
		Word wordC = null;

		while(wordA==null) {
			a = (int) (Math.random() * listAllWords.size());
			if(listAllWords.get(a).getSynonyms().size()>=1) {
				wordA = listAllWords.get(a);
			}
			else {
				String name = listAllWords.get(a).getName();
				RestTemplate restTemplate = new RestTemplate();
				String url = "https://api.datamuse.com/words?ml="+name;
				WordAPI[] list = restTemplate.getForObject(url, WordAPI[].class);
				int length = list.length;
				if(length>20) {
					length = 20;
				}
				for(int i=0; i<length;i++) {
					String nameSyn = WordUtil.correctString(list[i].getWord());
					for(int j=0; j<listAllWords.size();j++) {
						if(nameSyn.equals(listAllWords.get(j).getName())) {
							System.out.println("found A" + a);
							wordA = listAllWords.get(j);
							j = listAllWords.size();
							i = length;
						}
					}
				}
			}
			
		}
		
		while(wordB==null) {
			b = (int) (Math.random() * listAllWords.size());
			if(b!=a) {
				if(listAllWords.get(b).getSynonyms().size()>=1) {
					wordB = listAllWords.get(b);
				}
				else {
					String name = listAllWords.get(b).getName();
					RestTemplate restTemplate = new RestTemplate();
					String url = "https://api.datamuse.com/words?ml="+name;
					WordAPI[] list = restTemplate.getForObject(url, WordAPI[].class);
					int length = list.length;
					if(length>20) {
						length = 20;
					}
					for(int i=0; i<length;i++) {
						String nameSyn = WordUtil.correctString(list[i].getWord());
						for(int j=0; j<listAllWords.size();j++) {
							if(nameSyn.equals(listAllWords.get(j).getName())) {
								System.out.println("found B " + b);
								wordB = listAllWords.get(j);
								j = listAllWords.size();
								i = length;
							}
						}
					}
				}
			}
		}
		
		while(wordC==null) {
			c = (int) (Math.random() * listAllWords.size());
			if(c!=a && c!=b) {
				if(listAllWords.get(c).getSynonyms().size()>=1) {
					wordC = listAllWords.get(c);
				}
				else {
					String name = listAllWords.get(c).getName();
					RestTemplate restTemplate = new RestTemplate();
					String url = "https://api.datamuse.com/words?ml="+name;
					WordAPI[] list = restTemplate.getForObject(url, WordAPI[].class);
					int length = list.length;
					if(length>20) {
						length = 20;
					}
					for(int i=0; i<length;i++) {
						String nameSyn = WordUtil.correctString(list[i].getWord());
						for(int j=0; j<listAllWords.size();j++) {
							if(nameSyn.equals(listAllWords.get(j).getName())) {
								System.out.println("found C " + c);
								wordC = listAllWords.get(j);
								j = listAllWords.size();
								i = length;
							}
						}
					}
				}
			}
		}
		
		listRandomWords.add(wordA);
		listRandomWords.add(wordB);
		listRandomWords.add(wordC);

		System.out.println(listRandomWords.get(0).getName());
		System.out.println(listRandomWords.get(1).getName());
		System.out.println(listRandomWords.get(2).getName());

		
		return listRandomWords;
		
	}

	/**
	 * Methode getRandomAnswer : retourne une liste de mots (fausses reponses du quiz) qui sont aleatoires ET qui ne match pas avec le mot en question et la reponse synonyme
	 * @param noWord le mot concerne 
	 * @param noSynonym le synonyme concerne
	 * @return la liste de fausses reponses
	 */
	public List<Word> getRandomAnswer(String noWord, String noSynonym) {
		// TODO Auto-generated method stub
		List<Word> listAllWords = findAllWord();
		List<Word> listRandomAnwser = new ArrayList<>();
		String result = null;
		
		int a;
		do{
			a = (int)(Math.random() * listAllWords.size());
			result = listAllWords.get(a).getName();
		}while(result == noWord || result == noSynonym);
		
		int b;
		do{
			b = (int)(Math.random() * listAllWords.size());
			result = listAllWords.get(b).getName();
		}while(b == a || result == noWord || result == noSynonym);
		
		int c;
		do{
			c = (int)(Math.random() * listAllWords.size());
			result = listAllWords.get(c).getName();
		}while(c == b || c == a || result == noWord || result == noSynonym);
		
		int d;
		do{
			d = (int)(Math.random() * listAllWords.size());
			result = listAllWords.get(d).getName();
		}while(d == c || d == b || d == a || result == noWord || result == noSynonym);
		
		//System.out.println("valeur de a "+ a +" sur "+ listAllWords.size());
		//System.out.println("valeur de b "+ b +" sur "+ listAllWords.size());
		//System.out.println("valeur de c "+ c +" sur "+ listAllWords.size());
		//System.out.println("valeur de d "+ d +" sur "+ listAllWords.size());
		listRandomAnwser.add(listAllWords.get(a));
		listRandomAnwser.add(listAllWords.get(b));
		listRandomAnwser.add(listAllWords.get(c));
		listRandomAnwser.add(listAllWords.get(d));
        
		//System.out.println("la liste contient "+listRandomAnwser);
		return listRandomAnwser;
	}
	
	/**
	 * Methode updateWordWithList : met a jour les synonymes d'un mot grace a une liste
	 * @param name le nom du mot concerne
	 * @param synonyms les synonymes a ajouter
	 * @param language la langue du mot
	 */
	public void updateWordWithList(String name, List<String> synonyms, String language) {
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<synonyms.size();i++) {
			sb.append(synonyms.get(i));
			sb.append("_");
		}
		
		wordDao.updateWord(name, sb.toString(), language);
		System.out.println("UpdateList pour "+name);
		
	}
	
	/**
	 * Methode findAllNames : permet de recuperer tous les noms des mots de la bdd qui sont de la langue choisie
	 * @param language la langue
	 * @return la liste de noms
	 */
	public List<String> findAllNames(String language){
		return wordDao.findAllNames(language);
	}
	
	/**
	 * Methode containsWord : permet de verifier qu'un mot appartient a la bdd
	 * @param name le nom du mot
	 * @param language la langue du mot
	 * @param words la liste de noms des mots de la bdd
	 * @return true ou false
	 */
	public boolean containsWord(String name, String language, List<String> words) {
		if(words.contains(name)) {
			return true;
		}
		else 
			return false;
	}
	
	
}