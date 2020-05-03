package com.dicoloco.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
//import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.dicoloco.constant.Identifiant;
import com.dicoloco.model.Translation;
import com.dicoloco.model.Word;
import com.dicoloco.model.WordAPI;
import com.dicoloco.service.UserService;
import com.dicoloco.service.WordService;
import com.dicoloco.utils.WordUtil;

@RestController
@RequestMapping(path="/word")
public class WordController {
	
	private WordService wordService;
	
	@Value("${app.id}")
	private String id;
	
	@Value("${app.password}")
	private String password;
	
	
	@Autowired
	public WordController(WordService wordService) {
		this.wordService = wordService;
	}
	
	/**
	 * Permet de recuperer les identifiants de la bdd
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void getDatabaseSettingsFromProperties() {
	    Identifiant i = new Identifiant();
	    i.setId(id);
	    i.setPassword(password);
	}
	
	/**
	 * Methode translation : elle sert a recuperer la traduction d'un mot.
	 * @param name le nom du mot a traduire
	 * @param language la langue cible
	 * @return le mot trouve ou null
	 */
	@GetMapping(path="/translation/{name}/{language}")
	public Word translation(@PathVariable(name="name") String name, @PathVariable(name="language") String language) {
		RestTemplate restTemplate = new RestTemplate();
//		WordService w = new WordService();
		String languageSearch = language;
		if(language.equals("en")) {
			String temp = "fr-en";
			language = temp;
		}
		else if(language.equals("fr")) {
			String temp = "en-fr";
			language = temp;
		}
		
		String url = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20200414T080759Z.5dbe308c43e3f577.59033074ead6a56b5deaed63b8706cd9e08549fa" + 
				"&text="+name+"&lang="+language;
		Translation result = restTemplate.getForObject(url, Translation.class);
		
		Word found = null;
		
		if(result.getText().size()>0) {
			found = wordService.findWordByNameLanguage(WordUtil.correctString(result.getText().get(0)), languageSearch);
			/*
			if(found==null) {
				System.out.println("Search translation by synonyms");
				List<String> words = w.findAllNames(languageSearch);
				RestTemplate restTemplateSynonyms = new RestTemplate();
				String urlSynonyms = "https://api.datamuse.com/words?ml="+result.getText().get(0);
				WordAPI[] synonyms = restTemplateSynonyms.getForObject(urlSynonyms, WordAPI[].class);
				for(int i=0;i<synonyms.length;i++) {
					if(wordService.containsWord(WordUtil.getInstance().correctString(synonyms[i].getWord()), languageSearch, words)) {
						found = new Word(WordUtil.getInstance().correctString(synonyms[i].getWord()), languageSearch);
						i= synonyms.length;
					}
				}		
			}
			*/
		}
		return found;
	}
	
	
	/**
	 * Methode searchByLanguage : cherche le mot correspondant a un nom selon la langue.
	 * Si le mot n'a pas de synonyme dans la bdd,
	 * on fait un appel a Datamuse pour chercher ses synonymes et l'ecrire dans la base de donnees.
     * Seuls les synonymes appartenant a la base de donnees sont gardes.
	 * @param name le nom du mot a chercher
	 * @param language la langue du mot recherche
	 * @return le mot correspondant
	 */
	@GetMapping(path="/searchByLanguage/{name}/{language}")
	public Word searchByLanguage(@PathVariable(name="name") String name, @PathVariable(name="language") String language) {
		name = WordUtil.correctString(name);
		language = WordUtil.correctString(language);
		List<String> names = wordService.findAllNames(language);

		Word wordFound = wordService.findWordByNameLanguage(name, language, names);
		
		if(wordFound==null) {
			return null;
		}
		else {
			if(wordFound.getSynonyms().size()==0) {
				System.out.println("Ajout de synonymes pour "+name);
				RestTemplate restTemplate = new RestTemplate();
				String url = "https://api.datamuse.com/words?ml="+name;
				WordAPI[] list = restTemplate.getForObject(url, WordAPI[].class);
	
				int length;
				if(list.length>10) {
					length = 10;
				}
				else {
					length = list.length;
				}
				
				List<String> synonyms = new ArrayList<>();
				for(int i=0;i<length;i++) {
					if(wordService.containsWord(WordUtil.correctString(list[i].getWord()), language, names)) {
						String syn = list[i].getWord();
						if(syn.length()>1) {
							syn = syn.substring(0,1).toUpperCase() + syn.substring(1);
						}
						else
							syn = syn.toUpperCase();
						synonyms.add(syn);
						wordFound.getSynonyms().add(syn);
						if(wordFound.getSynonyms().size()>9) {
							i=length;
						}
					}
				}
				
				wordService.updateWordWithList(name, synonyms, language);
				
			}
			return wordFound;	
		}
	}
	
	/**
	 * Methode searchSuggestion : cherche des mots qui pourrait correspondre au mot tape.
	 * @param name le nom du mot tape
	 * @param language la langue du mot tape
	 * @return la liste de suggestions
	 */
	@GetMapping(path="/searchSuggestion/{name}/{language}")
	public List<Word> searchSuggestion(@PathVariable(name="name") String name, @PathVariable(name="language") String language) {
		return wordService.findSuggestionByName(name, language);
	}
	
	/**
	 * Methode getAllWords : recupere tous les mots de la base de donnees.
	 * @return la liste de tous les mots
	 */
	@GetMapping(path="/getAllWords")
	public List<Word> getAllWords(){
		return wordService.findAllWord();
	}
	
	/**
	 * Methode updateWord : Met a jour un mot en ajoutant ou supprimant un synonyme.
	 * @param name le nom du mot a mettre a jour
	 * @param synonym le synonyme a ajouter ou supprimer
	 * @param language la langue du mot a mettre a jour
	 * @param method : delete ou add : soit on ajoute, soit on supprime un synonyme
	 * @return le resultat : si 1, succes, si 2, le mot entre n'appartient pas a la bdd, si 3, pour delete, synonyme pas trouve dans la liste de ces synonymes,
	 * si 4, pour ajout, le synonyme fait deja parti de la liste de synonymes du mot, si 5, pour ajout, le synonyme n'existe pas dans la bdd
	 */
	@GetMapping(value= "update/{name}/{synonym}/{language}/{method}")
	public int updateWord(@PathVariable(name="name") String name, 
			@PathVariable(name="synonym") String synonym, @PathVariable(name="language") String language, @PathVariable(name="method") String method) {
		System.out.println("name "+name+"; synonym "+synonym+" ;language "+language+" ;method "+method+" ;");
		return wordService.updateWord(name, synonym, language, method);
	}
	
	/**
	 * Methode deleteWord : supprimer un mot de la bdd.
	 * @param name le nom du mot à supprimer
	 * @return le resultat : si 0, le mot a bien ete supprime, si 1, le mot n'a pas ete supprime, si 2, le mot n'existe pas dans la bdd
	 */
	@GetMapping(value= "delete/{name}/{language}")
	public int deleteWord(@PathVariable(name="name") String name, @PathVariable(name="language") String language) {
		name = WordUtil.correctString(name);
		int result = wordService.deleteWordService(name, language);
		if(result==0) {
			UserService u = new UserService();
			u.deleteFavoriteFromUsers(name, language);
		 }
		 return result;
	}

	/**
	 * Methode createListWords : ajoute une liste de mots.
	 * @param words la liste de mots
	 * @return le resultat : si 0, l'ajout s'est effectue, si 1, la liste n'a pas ete ajoutee,
	 * si 2, le mot existe deja dans la bdd (si la liste contient un mot)
	 */
	@PostMapping(value="/listWords",consumes = "application/json")
	public int createListWords(@RequestBody List<Word> words) {
		return wordService.createListWordService(WordUtil.transformListWordJSON(words));
	}
	
	/**
	 * Methode randomWord : permet de recuperer une liste de mots aleatoires.
	 * @return la liste de mots aleatoires
	 */
	@GetMapping(value= "/getRandom")
	public List<Word> randomWord() {
		return wordService.getRandomWord();
	}
	
	/**
	 * Methode randomAnswer : recupere une liste de mots qui contient le synonyme du mot indique et d'autres mots aleatoires
	 * @param noWord le mot concerne
	 * @param noSynonym le synonyme a ajouter dans la liste
	 * @return la liste de mots
	 */
	@GetMapping(value= "/getRandomAnswer/{noWord}/{noSynonym}")
	public List<Word> randomAnswer(@PathVariable(name="noWord") String noWord, @PathVariable(name="noSynonym") String noSynonym) {
		return wordService.getRandomAnswer(noWord, noSynonym);

	}
	
	
	
}
