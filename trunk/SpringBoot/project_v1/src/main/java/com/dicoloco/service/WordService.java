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
	 * Permet de mettre la premiere lettre du mot
	 * en majuscule
	 * @param value
	 */
	public static String upperCaseFirst(String value) {
        char[] listChar = value.toCharArray();
        listChar[0] = Character.toUpperCase(listChar[0]);
        return new String(listChar);
    }
	
	/**
	 * Cherche et retourne le mot recherche
	 * @param name
	 * @return le mot recherche ou null si pas trouve
	 */
	/*TODO
	public Word findWordByName(String name){
		String nameUpper = upperCaseFirst(name);
		Word found = wordDao.findWord(nameUpper);
		System.out.println("2found :"+found);
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
			return found;
		}
		return null;
	}
	*/
	
	/**
	 * Cherche et retourne le mot recherche selon la langue 
	 * @param name
	 * @param language
	 * @return le mot recherche ou null si pas trouve
	 */
	public Word findWordByNameLanguage(String name, String language){
		String nameUpper = upperCaseFirst(name);
		//TODO a modif peut etre
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
			return found;
		}
		return null;
	}
	
	/**
	 * Cherche et retourne les suggestions du mot recherche
	 * D'abord en remplaçant toutes les lettres une par une, par toute les lettres de l'alphabet
	 * Puis en regardant dans la liste de mot la bdd si un mot commence ou contient ce mot 
	 * @param name
	 * @param language
	 * @return liste des suggestions du mot recherche ou null si pas trouve
	 */
	public List<Word> findSuggestionByName(String name, String language){
		String nameUpper = (WordUtil.getInstance().correctString(name)).toLowerCase();
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
	 * Retourne la liste des mots de la bdd
	 * @return la liste des mots de la bdd
	 */
	public List<Word> findAllWord(){	
		return wordDao.findAllWords();
	}
	
	public List<Word> findAllWordWithLanguage(){
		return wordDao.findAllWordsWithLanguage("en");
	}
	
	/**
	 * Update la liste de synonymes d'un mot
	 * @param wordName
	 * @param wordSynonyms
	 * @param language
	 * @param method add ou delete
	 * @return 1 Ajout : Synonyme (Succes), 2 Pas de mot entree, 3 Delete : Synonyme pas trouve dans la liste de ces synonymes,
	 * 4 Ajout : Mais synonyme fait deja partie de sa liste de synonyme, 5 Ajout : Synonyme n'existe pas dans la bdd
	 */
	public int updateWord(String wordName, String wordSynonym, String language, String method) {
		wordSynonym = WordUtil.getInstance().correctString(wordSynonym);
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
	 * Supprime un mot de la bdd
	 * Retourne 2 si le mot a supprimer n'existe pas 
	 * @param name Nom du mot
	 * @return int Réponse de retour de la méthode
	 */
	public int deleteWordService(String name, String language) {
		String nameUpper = upperCaseFirst(name);
		
		if(wordDao.findWord(nameUpper, language) != null) {
			return wordDao.deleteWord(nameUpper, language);
		}
		
		return 2;
	}
	
	/**
	 * Créer un nouveau mot dans la bdd
	 * Retourne 0 si le mot a bien ete ajoute
	 * Retourne 1 si le mot n'a pas ete ajoute
	 * Retourne 2 si le mot existe deja
	 * @param name
	 * @param definitions : si egale a "_", definitions vide
	 * @param gender
	 * @param category
	 * @param synonyms : si egale a "_", aucun synonyme a ajouter
	 * @param language 
	 * @return int Reponse de retour de la méthode
	 */
	public int createWordService(String name, String definitions, String gender, String category, String synonyms, String language) {
		String nameUpper = WordUtil.getInstance().correctString(name);
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
	 * Ajout de liste de mots
	 * @param words : liste des mots du json
	 * @return 0 si tout va bien, 1 si erreur sql, 2 si le mot est deja dans la bdd
	 */
	public int createWordService2(List<Word> words) {
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
	
	/*
	 * Recupere une liste de 3 mots different et aleatoires possedant chacun au moins un synonyme
	 */
	public List<Word> getRandomWord() {
		List<Word> listAllWords = findAllWordWithLanguage();
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
					String nameSyn = this.correctString(list[i].getWord());
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
						String nameSyn = this.correctString(list[i].getWord());
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
						String nameSyn = this.correctString(list[i].getWord());
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
		
		/*
		do {
			a = (int)(Math.random() * listAllWords.size());
			//s'il n y a pas de synonym
			wordA = this.searchSynonyms(listAllWords.get(a), listAllWords);
			if((wordA.getSynonyms().size()) >= 1){
				isNotSyn = false ;
			}
		}while (isNotSyn);
			
		int b;
		isNotSyn = true;
		do{
			b = (int)(Math.random() * listAllWords.size());
			wordB = this.searchSynonyms(listAllWords.get(b), listAllWords);
			if((wordB.getSynonyms().size()) >= 1){
				isNotSyn = false ;
			}
		}while(b == a || isNotSyn);
		
		int c;
		isNotSyn = true;
		do{
			c = (int)(Math.random() * listAllWords.size());
			wordC = this.searchSynonyms(listAllWords.get(c), listAllWords);
			if((wordC.getSynonyms().size()) >= 1){
				isNotSyn = false ;
			}
		}while(c == b || c == a || isNotSyn);
		//System.out.println("valeur de a "+ a +" sur "+ listAllWords.size());
		//System.out.println("valeur de b "+ b +" sur "+ listAllWords.size());
		//System.out.println("valeur de c "+ c +" sur "+ listAllWords.size());
        listRandomWords.add(wordA);
        listRandomWords.add(wordB);
        listRandomWords.add(wordC);
        
        System.err.println("la liste contient "+listRandomWords);
		return listRandomWords;
		*/
		
	}

	/**
	 * retourne une liste de Word (fausse reponse du quiz) qui est aleatoire ET qui ne match pas avec les mot en question et la reponse synonyme
	 * @param noWord
	 * @param noSynonym
	 * @return
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
	
	public Word searchSynonyms(Word word, List<Word> words) {
		if(word.getSynonyms().size()==0) {
			System.err.println("SearchSynonyms pour "+word.getName());
			RestTemplate restTemplate = new RestTemplate();
			String url = "https://api.datamuse.com/words?ml="+word.getName();
			WordAPI[] list = restTemplate.getForObject(url, WordAPI[].class);

			int length;
			if(list.length>20) {
				length = 20;
			}
			else {
				length = list.length;
			}
			String synonym = null;
						
			for(int i=0;i<length;i++) {
//				System.out.println("synonyme possible : "+ this.correctString(list[i].getWord()));
				
				int count = 0;
				boolean continu = true;
				while(continu) {
					String nameSyn = correctString(list[i].getWord());
					System.out.println("Test pour "+nameSyn);

					if(count>=length) {
						continu = false;
//						System.err.println("pas d'action 1");
					}

					else if(words.get(count).getName().equals(nameSyn)){
						System.err.println("Equals");
						continu = false;
						synonym = list[i].getWord();
						i=length;
//						System.err.println("pas d'action 2");
					}
					else{
//						System.err.println("pas d'action 3");
					}
					count++;
				}
			}
			
			if(synonym!=null) {
				word.getSynonyms().add(synonym);
			}
		}
		else {
			System.out.println("le mot posse deja des synonyme");
		}
		
		return word;
	}
	
	public String correctString(String word) {
		String firstLetter = word;
		word = firstLetter.substring(0, 1).toUpperCase() + firstLetter.substring(1);
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
}