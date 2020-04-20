package com.dicoloco.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dicoloco.dao.UserDAO;
import com.dicoloco.model.User;
import com.dicoloco.model.Word;
import com.dicoloco.utils.WordUtil;

@Service
public class UserService {
	
	private UserDAO userDao = new UserDAO(); 
	
	@Autowired
	public UserService(@Qualifier("daoUser")UserDAO userDao) {
		this.userDao = userDao;
	}
	
	public UserService(){
		
	}
	
	/**
	 * Cherche l'utilisateur recherche
	 * @param name
	 * @return User l'utilisateur recherche ou null si pas trouve
	 */
	public User findUserAccount(String name){
		
        List<User> liste = userDao.getAllUsers();
		
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
		
        List<User> liste = userDao.getAllUsers();
        
        return liste;
	}
	
	/**
	 * Creer un utilisateur
	 * @param name
	 * @return 1 si reussite, sinon 0 si l'utilisateur existe deja
	 */
	public int createUser(String name) {
				
		if(this.findUserAccount(name)==null) {
			userDao.createUser(name);
			return 1;
		}
		else
			return 0;
	}
	
	
	/**
	 * Update la liste de favoris d'un utilisateur
	 * @param wordFavoris
	 * @param language
	 * @param userName
	 * @param methode add ou delete
	 * @return 1 succes, 2 user null, 3 favoris pas trouve, 4 favoris deja existant
	 */
	public int updateFavorites(String wordFavoris, String language, String userName, String method) {
		
		User user = userDao.findUserAccount(userName);
		
		if(user == null) {
			return 2;
		}
		else {
			List<String> favoris = user.getFavorites();

			StringBuffer sb = new StringBuffer();
			sb.append(wordFavoris+" | "+language);
			
			System.out.println("Avant : "+favoris+" method : "+method);
			if(method.equals("add")) {
				
				if(favoris.contains(sb.toString())) {
					return 4;
				}
				else {
					favoris.add(sb.toString());
				}
			}
			else if(method.equals("delete")){
				
				if(!favoris.contains(sb.toString())) {
					return 3;
				}
				else {
					for(int i=0; i<favoris.size(); i++) {
						
						if(favoris.get(i).equals(sb.toString())){
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
	
	/**
	 * Supprime un user de la bdd
	 * Retourne 0 si le user a bien ete supprime 
	 * Retourne 1 si le user n'a pas ete supprime 
	 * Retourne 2 si le user a supprimer n'existe pas 
	 * @param user Nom de le user
	 * @return int Reponse de retour de la methode
	 */
	public int deleteUserService(String user) {
		if(userDao.findUserAccount(user) != null) {
			return userDao.deleteUser(user);
		}
		return 2;
	}

	public void deleteFavoriteFromUsers(String name, String language) {
		List<User> users = userDao.getAllUsers();
		String temp = WordUtil.getInstance().correctString(name)+" | "+language;
		name = temp;
		System.out.println("Delete favorite : "+name);
		for(int i = 0; i<users.size(); i++) {
			List<String> favorites = users.get(i).getFavorites();
			for(int j=0; j<favorites.size();j++) {
				if(name.equals(favorites.get(j))) {
					favorites.remove(j);
				}
			}
		}
		
		userDao.removeAllUsers();
		userDao.addUsers(users);
	}

}
