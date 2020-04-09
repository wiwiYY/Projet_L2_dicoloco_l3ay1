package com.dicoloco.service;

import java.util.List;

import com.dicoloco.dao.UserDAO;
import com.dicoloco.model.User;

public class UserService {
	
	public UserService() {
		
	}
	
	/**
	 * Cherche l'utilisateur recherche
	 * @param name
	 * @return l'utilisateur recherche ou null si pas trouve
	 */
	public User findUserAccount(String name){
		
		UserDAO dao = new UserDAO();
        List<User> liste = dao.getAllUsers();
		
		for(int i=0; i<liste.size(); i++) {
			if(liste.get(i).getName().equals(name)) {
				return liste.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Retourne la liste des utilisateurs de la bdd
	 * @return la liste des utilisateurs de la bdd
	 */
	public List<User> findAllUsers(){
		
		UserDAO dao = new UserDAO();
        List<User> liste = dao.getAllUsers();
        
        return liste;
	}
	
	/**
	 * Creer un utilisateur
	 * @param name
	 * @return 1 si reussite, sinon 0 si echec
	 */
	public int createUser(String name) {
		
		UserDAO u = new UserDAO();
		
		if(this.findUserAccount(name)==null) {
			u.createUser(name);
			return 1;
		}
		else
			return 0;
	}
	
	/**
	 * Update la liste de favoris d'un utilisateur
	 * @param wordFavoris
	 * @param userName
	 * @param methode add ou delete
	 * @return 1 succes, 2 user null, 3 favoris pas trouve, 4 favoris deja existant
	 */
	public int updateFavorites(String wordFavoris, String userName, String method) {
		
		UserDAO userDao = new UserDAO();
		User user = userDao.findUserAccount(userName);
		
		if(user == null) {
			return 2;
		}
		else {
			List<String> favoris = user.getFavorites();
			System.out.println("Avant : "+favoris+" method : "+method);
			if(method.equals("add")) {
				
				if(favoris.contains(wordFavoris)) {
					return 4;
				}
				else {
					favoris.add(wordFavoris);
				}
			}
			else if(method.equals("delete")){
				
				if(!favoris.contains(wordFavoris)) {
					return 3;
				}
				else {
					for(int i=0; i<favoris.size(); i++) {
						
						if(favoris.get(i).equals(wordFavoris)){
							favoris.remove(i);
						}
					}
				}
				System.out.println("Après : "+favoris);
			}
			
			StringBuffer favorisBuffer = new StringBuffer();
			for(int i=0; i<favoris.size(); i++) {
				favorisBuffer.append(favoris.get(i));
				favorisBuffer.append("_");
			}
			
			userDao.updateFavoritesList(userName, favorisBuffer.toString());
			return 1;
		}
	}
}
