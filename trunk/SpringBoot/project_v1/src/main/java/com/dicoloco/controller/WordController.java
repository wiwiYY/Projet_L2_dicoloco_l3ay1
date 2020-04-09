package com.dicoloco.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.dicoloco.model.WordJSON;
import com.dicoloco.model.Word;
import com.dicoloco.model.WordAPI;
import com.dicoloco.service.WordService;

@RestController
@RequestMapping(path="/word")
public class WordController {
	
	private WordService wordService;
	
	@Autowired
	public WordController(WordService wordService) {
		this.wordService = wordService;
	}
	
	
	/**
	 * Cherche le mot correspondant a name
	 * @param name
	 * @return le mot correspondant
	 */
	@GetMapping(path="/search/{name}")
	public Word search(@PathVariable(name="name") String name) {
		
		Word wordFound = wordService.findWordByName(name);
		if(wordFound==null) {
			return null;
		}

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
			for(int i=0;i<length;i++) {
				if(wordService.findWordByName(list[i].getWord())!=null) {
					wordService.updateWord(name, list[i].getWord(), "add");
					wordFound.getSynonyms().add(list[i].getWord());
				}
			}
			System.out.println(wordService.findWordByName(name).getSynonyms());
		}
		return wordService.findWordByName(name);
	}
	
	/**
	 * Cherche les suggestions d'un mot tape
	 * @param name
	 * @return La liste de suggestions
	 */
	@GetMapping(path="/searchSuggestion/{name}")
	public List<Word> searchSuggestion(@PathVariable(name="name") String name) {
		
		return wordService.findSuggestionByName(name);
	}
	
	/**
	 * Recupere tous les mots
	 * @return liste de tous les mots
	 */
	@GetMapping(path="/getAllWords")
	public List<Word> getAllWords(){
		return wordService.findAllWord();
	}
	
	/**
	 * Update un mot en ajoutant ou supprimant un synonyme
	 * @param name
	 * @param synonym
	 * @param method : delete ou add
	 * @return 1 Ajout : Synonyme (Succes), 2 Pas de mot entree, 3 Delete : Synonyme pas trouve dans la liste de ces synonymes,
	 * 4 Ajout : Mais synonyme fait deja partie de sa liste de synonyme, 5 Ajout : Synonyme n'existe pas dans la bdd
	 */
	@GetMapping(value= "update/{name}/{synonym}/{method}")
	public int updateWord(@PathVariable(name="name") String name, 
			@PathVariable(name="synonym") String synonym, @PathVariable(name="method") String method) {
		System.out.println("name "+name+"; synonym "+synonym+" ;method "+method+" ;");
		return wordService.updateWord(name, synonym, method);
	}
	
	/**
	 * Les def et les synonymes des mots sont separees par des _ du 8 
	 * @param name
	 * @param definitions
	 * @param gender
	 * @param category
	 * @param language
	 * @return int Réponse de retour de la méthode 
	 */
	@GetMapping(value ="create/{name}/{definitions}/{gender}/{category}/{synonyms}/{language}")
	public int createWord(@PathVariable(name="name") String name, @PathVariable(name="definitions") String definitions,
			@PathVariable(name="gender") String gender, @PathVariable(name="category") String category, 
			@PathVariable(name="language") String language, @PathVariable(name="synonyms") String synonyms) {
		return wordService.createWordService(name, definitions, gender, category, synonyms, language);
	}

	/**
	 * Supprimer un mot de la bdd
	 * Retourne 0 si le mot a bien été supprimé 
	 * Retourne 1 si le mot n'a pas été supprimé 
	 * Retourne 2 si le mot à supprimer n'existe pas 
	 * @param name Mot à supprimer
	 * @return int Réponse de retour de la méthode 
	 */
	@GetMapping(value= "delete/{name}")
	public int deleteWord(@PathVariable(name="name") String name) {
		return wordService.deleteWordService(name);
	}
	
	/**
	 * Ajoute une liste de mots grace a un JSON
	 * @param words recupere grace au JSON
	 */
	@PostMapping(value="/listWords",consumes = "application/json" )
	public void createListWords(@RequestBody List<Word> words) {
		for(int nb=0; nb<words.size(); nb++) {
			List<String> definitions = new ArrayList<>();
			
			for(int i=0; i< words.get(nb).getDefinition().size(); i++) {
				definitions.add(this.appendDefWithCategory(words.get(nb).getCategory(),words.get(nb).getDefinition().get(i)));
			}	
			words.get(nb).setDefinition(definitions);			
		}
		wordService.createWordService2(words);
	}
	
	
	/**
	 * Permet de coller la definition avec la categorie qui correspond
	 * @param typeWord
	 * @param definition
	 * @return
	 */
	public String appendDefWithCategory(String typeWord, String definition) {
		//Permet de retirer les { de la def car ils posent des problemes pour SQL
		if(definition.contains("{")) {
			String temp = definition.replace("{", "");
			definition = temp;
		}
		
		if(typeWord.contains("n.") || typeWord.contains("ambassade.")||typeWord.equals("p.")||typeWord.equals("n")) {
			return ("Nom : " + definition);
		}
		else if(typeWord.contains("Indic.")||typeWord.contains("sing.")||typeWord.contains("p.")||typeWord.contains("v.")|| typeWord.contains("imp.")||typeWord.contains("pres.") ||typeWord.contains("indic.")||typeWord.contains("infinitive.")) {
			return ("Verbe : " + definition);
		}
		else if(typeWord.contains("a.") || typeWord.contains("superl")|| typeWord.contains("super.")||typeWord.contains("adj.")||typeWord.contains("Superl.")) {
			return ("Adjectif : " + definition);
		}
		else if(typeWord.contains("Compar.")) {
			return ("Preposition : " + definition);
		}
		else if(typeWord.contains("prep.")) {
			return ("Preposition : " + definition);
		}
		else if(typeWord.contains("conj.")) {
			return ("Conjonction : " + definition);
		}
		else if(typeWord.contains("suffix.")) {
			return ("Suffixe : " + definition);
		}
		else if(typeWord.contains("prefix.")||typeWord.contains("pref.")) {
			return ("Prefixe : " + definition);
		}
		else if(typeWord.contains("obj.")) {
			return ("Pronom : " + definition);
		}
		else if(typeWord.contains("pl.")) {
			return ("Nom pluriel : " + definition);
		}
		else if(typeWord.contains("adv.")||typeWord.contains("ad.")||typeWord.contains("ads.")) {
			return ("Adverbe : " + definition);
		}
		else if(typeWord.contains("interj.")) {
			return ("Interjonction : " + definition);
		}
		else if(typeWord.contains("nothing.")) {
			return ("Type inconnu : " + definition);
		}
		
		else {
			System.out.println(typeWord);
			return "Type inconnu : " + definition;
		}
	}
	
}
