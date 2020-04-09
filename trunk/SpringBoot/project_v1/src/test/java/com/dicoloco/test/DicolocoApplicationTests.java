package com.dicoloco.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.dicoloco.dao.WordDao;
import com.dicoloco.model.Word;
import com.dicoloco.service.WordService;



@SpringBootTest
class DicolocoApplicationTests {

	
	@Autowired
	private WordService wordService;
	
	@MockBean
	private WordDao wordDao;
	
	
	@Test
	public void findAllWordsTest() {
		
		List<String> definitions = new ArrayList<String>();
		definitions.add("defTest1");
		definitions.add("defTest2");
		
		
		List<String> synonyms = new ArrayList<String>();
		definitions.add("synTest1");
		definitions.add("synTest2");
		
		when(wordDao.findAllWords()).thenReturn(Stream
				.of(new Word("Test1", definitions, "rien", "nom", synonyms, "en"), 
					new Word("Test2", definitions, "rien", "nom", synonyms, "en")).collect(Collectors.toList()));
		
		assertEquals(2, wordService.findAllWord().size());
		
	}
	
	@Test
	public void findWordByNameTest() {
		
		
		List<String> definitions = new ArrayList<String>();
		definitions.add("defTest1");
		definitions.add("defTest2");
		
		
		List<String> synonyms = new ArrayList<String>();
		definitions.add("synTest1");
		definitions.add("synTest2");
		
		Word word = new Word("Test1", definitions, "rien", "nom", synonyms, "en");	
		
		when(wordDao.findWord(word.getName())).thenReturn(word);
		assertEquals(word, wordService.findWordByName(word.getName()));
	}
	
	@Test
	public void createWordTest() {
		assertEquals(1,wordService.createWordService("Test", "definitions", "rien", "nom", "synonyms", "en") );
	}
	
	
	@Test
	public void deleteWordTest() {
		
		List<String> definitions = new ArrayList<String>();
		definitions.add("defTest1");
		definitions.add("defTest2");
		
		
		List<String> synonyms = new ArrayList<String>();
		definitions.add("synTest1");
		definitions.add("synTest2");
		
		Word word= new Word("Test", definitions, "rien", "nom", synonyms, "en");
		
		assertEquals(2,wordService.deleteWordService(word.getName()) );
	}
	
}
