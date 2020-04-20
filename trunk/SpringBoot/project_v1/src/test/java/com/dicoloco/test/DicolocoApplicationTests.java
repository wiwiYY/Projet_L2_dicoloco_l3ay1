package com.dicoloco.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.dicoloco.dao.WordDao;
import com.dicoloco.model.Word;
import com.dicoloco.service.WordService;


/*
 * Tests unitaires pour la classe WordService
 */
@SpringBootTest
class DicolocoApplicationTests {

	
	@Autowired
	private WordService wordService;
	
	@MockBean
	private WordDao wordDao;
	
	@BeforeEach
    void setUp() {
        wordService = new WordService(wordDao);
    }
	
	@Test
	public void findAllWordsTest() {
		
		List<String> definitions = new ArrayList<String>();
		definitions.add("defTest1");
		definitions.add("defTest2");
		
		
		List<String> synonyms = new ArrayList<String>();
		synonyms.add("synTest1");
		synonyms.add("synTest2");
		
		when(wordDao.findAllWords()).thenReturn(Stream
				.of(new Word("Test1", definitions, "rien", "nom", synonyms, "en"), 
					new Word("Test2", definitions, "rien", "nom", synonyms, "en")).collect(Collectors.toList()));
		
		assertEquals(2, wordService.findAllWord().size());
		
	}
	
	
	
	@Test
	public void createWordTest() {
		assertEquals(1,wordService.createWordService("Test", "definitions", "rien", "nom", "synonyms", "en") );
	}
	
	@Test
	public void findWordByNameTest() {
		
		
		List<String> definitions = new ArrayList<String>();
		definitions.add("defTest1");
		definitions.add("defTest2");
		
		
		List<String> synonyms = new ArrayList<String>();
		synonyms.add("synTest1");
		synonyms.add("synTest2");
		
		Word word = new Word("Test", definitions, "rien", "nom", synonyms, "en");	
		
		when(wordDao.findWord(word.getName(), word.getLanguage())).thenReturn(word);
		assertEquals(word, wordService.findWordByNameLanguage(word.getName(),word.getLanguage()));
	}
	
	@Test
	public void deleteWordTest() {
		
		List<String> definitions = new ArrayList<String>();
		definitions.add("defTest1");
		definitions.add("defTest2");
		
		
		List<String> synonyms = new ArrayList<String>();
		synonyms.add("synTest1");
		synonyms.add("synTest2");
		
		Word word= new Word("Test", definitions, "rien", "nom", synonyms, "en");
		
		when(wordDao.findWord(word.getName(), word.getLanguage())).thenReturn(word);
		assertEquals(0,wordService.deleteWordService(word.getName(), "en"));
	}
	
	@Test
	public void addSynonymWordTest() {
		
		List<String> definitions = new ArrayList<String>();
		List<String> synonyms = new ArrayList<String>();
		definitions.add("defTest1");
		synonyms.add("synTest1");
		Word word= new Word("Test3", definitions, "rien", "nom", synonyms, "en");
		Word word1= new Word("Test4", definitions, "rien", "nom", synonyms, "en");
		
		when(wordDao.findWord(word.getName(), word.getLanguage())).thenReturn(word);
		when(wordDao.findWord(word1.getName(), word1.getLanguage())).thenReturn(word1);
		assertEquals(1,wordService.updateWord(word.getName(), word1.getName(), "en", "add") );
	}
	
	@Test
	public void deleteSynonymWordTest() {
		
		List<String> definitions = new ArrayList<String>();
		List<String> synonyms = new ArrayList<String>();
		definitions.add("defTest1");
		synonyms.add("synTest1");
		Word word2= new Word("Test5", definitions, "rien", "nom", synonyms, "en");
		Word word3= new Word("Test6", definitions, "rien", "nom", synonyms, "en");
		
		when(wordDao.findWord(word2.getName(), word2.getLanguage())).thenReturn(word2);
		when(wordDao.findWord(word3.getName(), word3.getLanguage())).thenReturn(word3);
		assertEquals(3,wordService.updateWord(word2.getName(), word3.getName(), "en", "delete") );
	}
	
}
