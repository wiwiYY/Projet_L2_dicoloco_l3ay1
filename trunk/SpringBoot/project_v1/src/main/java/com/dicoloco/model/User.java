package com.dicoloco.model;

import java.util.List;

/*
 * Classe User pour le model d'utilisateur
 */
public class User {
	 
    private String name;
    private List<String> favorites;
    
    public User() {
 
    }
 
    public User(String name, List<String> favorites) {
        this.name = name;
        this.favorites = favorites;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public List<String> getFavorites() {
        return favorites;
    }
 
    public void setFavorites(List<String> favorites) {
        this.favorites = favorites;
    }

    @Override
    public String toString() {
        return this.name + "/" + this.favorites;
    }
 
}