package com.dicoloco.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dicoloco.dao.WordDao;
import com.dicoloco.model.Word;

@Service
public class WordService {
	
	private WordDao wordDao;
	
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
	public Word findWordByName(String name){
		String nameUpper = upperCaseFirst(name);
		Word found = wordDao.findWord(nameUpper);
		
		List<String> definitions = new ArrayList<>();
		
		if(found != null) {
			for(int i=0; i<found.getDefinition().size(); i++) {
				StringBuffer defBuffer = new StringBuffer();
				
				String definitionFound = found.getDefinition().get(i);
				definitionFound = definitionFound.replace("\n", "");
				
				defBuffer.append(definitionFound);
				defBuffer.append("_");
				
				definitions.add(i, defBuffer.toString());
			}
			found.setDefinition(definitions);
		}
		return found;
	}
	
	/**
	 * Cherche et retourne les suggestions du mot recherche
	 * D'abord en remplaçant toutes les lettres une par une, par toute les lettres de l'alphabet
	 * Puis en regardant dans la liste de mot la bdd si un mot commence ou contient ce mot 
	 * @param name
	 * @return liste des suggestions du mot recherche ou null si pas trouve
	 */
	public List<Word> findSuggestionByName(String name){
		String nameUpper = upperCaseFirst(name);
		String partNameNotChangePre = null;
		String partNameNotChangePost = null; 
		String partNameChange = null;
		
		final int TAILLE_TAB = 10;
		List<Word> listSugg = new ArrayList<Word>();
		Word word = null; 
		
		List<Word> bdd = wordDao.findAllWords();
		
		for(int j = 1; j<=nameUpper.length(); j++) {	
			for(int i = 97; i<=122 ; i++) {
				partNameNotChangePre = nameUpper.substring(0, j-1);
				partNameNotChangePost = nameUpper.substring(j, nameUpper.length());				
				partNameChange = Character.toString((char)i);
				
				for(int k=0; k<bdd.size(); k++) {
					if((partNameNotChangePre.concat(partNameChange.concat(partNameNotChangePost))).equals(bdd.get(k).getName())) {
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
	
	/**
	 * Update la liste de synonymes d'un mot
	 * @param wordName
	 * @param wordSynonyms
	 * @param method add ou delete
	 * @return 1 Ajout : Synonyme (Succes), 2 Pas de mot entree, 3 Delete : Synonyme pas trouve dans la liste de ces synonymes,
	 * 4 Ajout : Mais synonyme fait deja partie de sa liste de synonyme, 5 Ajout : Synonyme n'existe pas dans la bdd
	 * 6 Erreur methode
	 */
	public int updateWord(String wordName, String wordSynonym, String method) {
		
		Word word = wordDao.findWord(wordName);
		
		Word wordSyn = wordDao.findWord(wordSynonym);
		
		if(word == null) { 
			return 2;
		}
		else {
			List<String> synonyms = word.getSynonyms();
			if(method.equals("add")) { 
				
				if(synonyms.contains(wordSynonym)) { 
					return 4;
				}
				else if(wordSyn == null) { 
					return 5;
				}
				else {
					synonyms.add(wordSynonym);
					word.setChangedSynonym(true);
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
				return 6;
			
			StringBuffer synonymsBuffer = new StringBuffer();
			for(int i=0; i<synonyms.size(); i++) {
				synonymsBuffer.append(synonyms.get(i));
				synonymsBuffer.append("_");
			}
			wordDao.updateWord(wordName, synonymsBuffer.toString());
			System.out.println("Mise à jour des synonymes avec "+synonymsBuffer);
			return 1;
		}
	}
	
	/**
	 * Supprime un mot de la bdd
	 * Retourne 2 si le mot a supprimer n'existe pas 
	 * @param name Nom du mot
	 * @return int Réponse de retour de la méthode
	 */
	public int deleteWordService(String name) {
		String nameUpper = upperCaseFirst(name);
		
		if(wordDao.findWord(nameUpper) != null) {
			return wordDao.deleteWord(nameUpper);
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
		String nameUpper = upperCaseFirst(name);
		
		if(wordDao.findWord(nameUpper) == null) {
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
		
		if(wordDao.findWord(nameUpper) == null) {
			return 1;
		} else {
			return 0;
		}
	}
	
	/**
	 * 
	 * @param words : liste des mots du json
	 */
	public void createWordService2(List<Word> words) {
		List<Word> wordsDB = wordDao.findAllWords();
		
		for(int i=0; i<wordsDB.size(); i++) {
			for(int j=0; j<words.size(); j++) {
				
				//on va comparer tous les mots de la bdd dans la db avec les mots du JSON
				if(wordsDB.get(i).getName().equals(words.get(j).getName())) {
					words.remove(j);
				}
			}
		}
		wordDao.createWordBDDFound(words);
	}
}
