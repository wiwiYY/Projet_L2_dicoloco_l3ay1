package com.dicoloco.model;

import java.util.ArrayList;

public class Translation {
	 private float code;
	 private String lang;
	 ArrayList <String> text = new ArrayList <String>();
	 

	public void setText(ArrayList<String> text) {
		this.text = text;
	}

	public float getCode() {
	  return code;
	 }

	 public String getLang() {
	  return lang;
	 }

	 public void setCode(float code) {
	  this.code = code;
	 }

	 public void setLang(String lang) {
	  this.lang = lang;
	 }

	public ArrayList<String> getText() {
		// TODO Auto-generated method stub
		return text;
	}
	}