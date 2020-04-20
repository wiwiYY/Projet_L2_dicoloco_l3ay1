package com.dicoloco.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
	
	@Autowired
	public WordController(WordService wordService) {
		this.wordService = wordService;
	}
	
	@RequestMapping("/test")
	@ResponseBody
	String test() {
		return "The application is running!";
	}
	
	/**
	 * @param name le nom du mot a traduire
	 * @param language la langue cible
	 * @return le mot trouve ou null
	 */
	@GetMapping(path="/translation/{name}/{language}")
	public Word translation(@PathVariable(name="name") String name, @PathVariable(name="language") String language) {
		RestTemplate restTemplate = new RestTemplate();
		WordService w = new WordService();
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
			found = this.searchByLanguage(result.getText().get(0), languageSearch);
			if(found==null) {
				RestTemplate restTemplateSynonyms = new RestTemplate();
				String urlSynonyms = "https://api.datamuse.com/words?ml="+result.getText().get(0);
				WordAPI[] synonyms = restTemplateSynonyms.getForObject(urlSynonyms, WordAPI[].class);
				System.out.println(synonyms.length);
				boolean continu = true;
				int count = 0;
				
				while(continu) {
					if(synonyms.length==0) {
						continu = false;
					}
					if(count>=synonyms.length) {
						continu = false;
					}
					else {
						if(count<synonyms.length) {
							if(synonyms[count]==null) {
								continu = false;
							}
							else {
								Word foundSynonyms = w.findWordByNameLanguage(synonyms[count].getWord(), languageSearch);
								if(foundSynonyms == null) {
									found = foundSynonyms;
									continu = false;
								}
								else {
									count++;
								}
							}
						}
						else {
							continu = false;
						}
					}
				}
			}
		}
		return found;
	}
	
	
	/**
	 * Cherche le mot correspondant a name selon le language 
	 * Si le mot n'a pas de synonyme dans la bdd
	 * On fait un appel a Datamuse pour chercher ses synonymes et l'ecrire dans la bdd 
     * Comme cela la prochaine fois que l'on cherchera ce mot, ces synonymes seront deja dans la bdd 
     * La bdd cherche des synonymes seulement si le mot n'en possede pas
	 * @param name
	 * @param language
	 * @return le mot correspondant
	 */
	@GetMapping(path="/searchByLanguage/{name}/{language}")
	public Word searchByLanguage(@PathVariable(name="name") String name, @PathVariable(name="language") String language) {
		name = WordUtil.getInstance().correctString(name);
		language = WordUtil.getInstance().correctString(language);
		Word wordFound = wordService.findWordByNameLanguage(name, language);
		
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
				if(list.length>20) {
					length = 20;
				}
				else {
					length = list.length;
				}
				
				List<String> synonyms = new ArrayList<>();
				for(int i=0;i<length;i++) {
					if(wordService.findWordByNameLanguage(list[i].getWord(), language)!=null) {
						wordService.updateWord(name, list[i].getWord(), language, "add");
						synonyms.add(list[i].getWord());
						wordFound.getSynonyms().add(list[i].getWord());
						if(wordFound.getSynonyms().size()>9) {
							i=length;
						}
					}
				}
				
				System.out.println(wordService.findWordByNameLanguage(name, language).getSynonyms());
			}
			return wordFound;	
		}
	}
	
	/**
	 * Cherche les suggestions d'un mot tape
	 * @param name
	 * @param language
	 * @return La liste de suggestions
	 */
	@GetMapping(path="/searchSuggestion/{name}/{language}")
	public List<Word> searchSuggestion(@PathVariable(name="name") String name, @PathVariable(name="language") String language) {
		return wordService.findSuggestionByName(name, language);
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
	 * @param language
	 * @param method : delete ou add
	 * @return 1 Ajout : Synonyme (Succes), 2 Pas de mot entree, 3 Delete : Synonyme pas trouve dans la liste de ces synonymes,
	 * 4 Ajout : Mais synonyme fait deja partie de sa liste de synonyme, 5 Ajout : Synonyme n'existe pas dans la bdd
	 */
	@GetMapping(value= "update/{name}/{synonym}/{language}/{method}")
	public int updateWord(@PathVariable(name="name") String name, 
			@PathVariable(name="synonym") String synonym, @PathVariable(name="language") String language, @PathVariable(name="method") String method) {
		System.out.println("name "+name+"; synonym "+synonym+" ;language "+language+" ;method "+method+" ;");
		return wordService.updateWord(name, synonym, language, method);
	}
	
	/**
	 * Supprimer un mot de la bdd
	 * Retourne 0 si le mot a bien été supprimé 
	 * Retourne 1 si le mot n'a pas été supprimé 
	 * Retourne 2 si le mot à supprimer n'existe pas 
	 * 
	 * @param name Mot à supprimer
	 * @return int Réponse de retour de la méthode 
	 */
	@GetMapping(value= "delete/{name}/{language}")
	public int deleteWord(@PathVariable(name="name") String name, @PathVariable(name="language") String language) {
		 int result = wordService.deleteWordService(name, language);
		 if(result==0) {
			UserService u = new UserService();
			u.deleteFavoriteFromUsers(name, language);
		 }
		 return result;
	}

	/**
	 * Ajoute une liste de mots grace a un JSON
	 * @param mots recuperes grace au JSON
	 */
	@PostMapping(value="/listWords",consumes = "application/json")
	public int createListWords(@RequestBody List<Word> words) {
		return wordService.createWordService2(this.transformListWordJSON(words));
	}
	
	
	
	/**
	 * TODO Modif
	 * Permet de coller la definition avec la categorie qui correspond
	 * Corrige la synthaxe si necessaire du type et de la definition
	 * Si le mot est anglais, on ne prend pas en compte le genre
	 * @param typeWord
	 * @param definitions
	 * @return
	 */
	
	public String appendDefWithCategory(String typeWord, String definitions, String gender, String language) {
		if(language.equals("en")) {
			String tempType = WordUtil.getInstance().correctString(typeWord);	
			String tempDef = WordUtil.getInstance().correctString(definitions);	

			return tempType + " : " + tempDef;
		}
		
		else if(language.equals("fr")) {
			if(gender.equals("")) {
				String tempType = WordUtil.getInstance().correctString(typeWord);	
				String tempDef = WordUtil.getInstance().correctString(definitions);	

				return tempType + " : " + tempDef;
			}
			else {
				String tempType = WordUtil.getInstance().correctString(typeWord);	
				String tempDef = WordUtil.getInstance().correctString(definitions);	
				String tempGender = WordUtil.getInstance().correctString(gender);
				return tempType + " "+ tempGender+ " : " + tempDef;
			}
		}
		else {
			String tempType = WordUtil.getInstance().correctString(typeWord);	
			String tempDef = WordUtil.getInstance().correctString(definitions);	

			return tempType + " : " + tempDef;
		}
	}
	
	
	@GetMapping(value= "/getRandom")
	public List<Word> randomWord() {
		return wordService.getRandomWord();
	}
	@GetMapping(value= "/getRandomAnswer/{noWord}/{noSynonym}")
	public List<Word> randomAnswer(@PathVariable(name="noWord") String noWord, @PathVariable(name="noSynonym") String noSynonym) {
		return wordService.getRandomAnswer(noWord, noSynonym);

	}
	
	
	/**
	 * TODO Modif
	 * Permet de classer les categories à côté des définitions
	 * La BDD possède des mots qui ont le même nom, donc on récupère 
	 * leur définitions et categories et on les ajoute dans un seul mot
	 * @param wordsJSON
	 * @return une liste Word 
	 */
	public List<Word> transformListWordJSON(List<Word> wordsFirst){
		List<Word> words = new ArrayList<>(); 
		
		for(int i=0; i<wordsFirst.size(); i++) {
			
			List<String> definitions = new ArrayList<>();
			List<String> definitionsFirst = wordsFirst.get(i).getDefinitions();
			for(int h=0;h<definitionsFirst.size(); h++) {
				definitions.add(this.appendDefWithCategory(wordsFirst.get(i).getCategory(), definitionsFirst.get(h), wordsFirst.get(i).getGender(), wordsFirst.get(i).getLanguage()));
			}
			List<String> synonyms = new ArrayList<>();
			List<String> synonymsFirst = wordsFirst.get(i).getSynonyms();
			for(int h=0;h<synonymsFirst.size(); h++) {
				synonyms.add(WordUtil.getInstance().correctString(synonymsFirst.get(h)));
			}

			String name = WordUtil.getInstance().correctString(wordsFirst.get(i).getName());
			wordsFirst.get(i).setName(name);
			
			for(int j=i+1; j<wordsFirst.size();j++) {
				String temp = wordsFirst.get(j).getName();
				wordsFirst.get(j).setName(WordUtil.getInstance().correctString(temp));
				
				if(wordsFirst.get(i).getName().equals(wordsFirst.get(j).getName())) {
							
					String typeOther = wordsFirst.get(j).getCategory();
					List<String> defOther = wordsFirst.get(j).getDefinitions();
						
					for(int k=0; k<defOther.size(); k++) {
						definitions.add(this.appendDefWithCategory(typeOther, wordsFirst.get(j).getDefinitions().get(k), wordsFirst.get(j).getGender(), wordsFirst.get(j).getLanguage()));
					}
					
					wordsFirst.remove(j);
					j--;
					
				}
			}			
			
			//TODO A modif pour le modele finale
			words.add(new Word(wordsFirst.get(i).getName(), definitions, "Genre a cote des definitions", "Categorie a cote des definitions", synonyms , wordsFirst.get(i).getLanguage()));
		}
		
		return words;
		
	}
	

}
