package com.dicoloco.model;

import java.util.ArrayList;

/*
 * Classe WordAPI utilisée pour l'api Datamuse 
 * 
 */
public class WordAPI {
	 private String word;
	 private float score;
	 ArrayList<Object> tags = new ArrayList <Object> ();


	 // Getter Methods 

	 public String getWord() {
	  return word;
	 }

	 public float getScore() {
	  return score;
	 }

	 // Setter Methods 

	 public void setWord(String word) {
	  this.word = word;
	 }

	 public void setScore(float score) {
	  this.score = score;
	 }

	
	}