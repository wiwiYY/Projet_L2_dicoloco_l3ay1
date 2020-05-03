package com.dicoloco.service;

//import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dicoloco.dao.UserDAO;
import com.dicoloco.model.User;
//import com.dicoloco.model.Word;
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
	 * Methode findUserAccount : Cherche l'utilisateur par appel de la methode userDao
	 * @param name le nom de l'utilisateur
	 * @return User les informations l'utilisateur ou null si pas trouve
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
	 * Method findAllUsers : Retourne la liste des utilisateurs de la base de donnee par appel de la methode userDao
	 * @return la liste des utilisateurs
	 */
	public List<User> findAllUsers(){
		
        List<User> liste = userDao.getAllUsers();
        
        return liste;
	}
	
	/**
	 * Methode createUser : Creer un utilisateur par appel de la methode userDao
	 * @param name le nom de l'utilisateur
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
	 * Methode updateFavorites : met a jour la liste de favoris d'un utilisateur par appel de la methode userDao
	 * @param wordFavoris le mot favoris
	 * @param language la langue du mot en favoris
	 * @param userName le nom de l'utilisateur
	 * @param methode add ou delete la methode de mise a jour ajout ou suppression
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
	 * Methode deleteUserService : Supprime un utilisateur de la base de donnee par appel de la methode userDao
	 * Retourne 0 si le user a bien ete supprime 
	 * Retourne 1 si le user n'a pas ete supprime 
	 * Retourne 2 si le user a supprimer n'existe pas 
	 * @param user Nom de l'utilisateur
	 * @return int Reponse de retour de la methode
	 */
	public int deleteUserService(String user) {
		
		if(userDao.findUserAccount(user) != null) {
			return userDao.deleteUser(user);
		}
		return 2;
	}

	/**
	 * Methode deleteFavoriteFromUsers : Cette methode supprime un mot des favoris de tous 
	 * les utilisateurs de la base de donnee par appel de la methode userDao
	 * @param name le mot a supprimer de chaque favoris
	 * @param language la langue du mot
	 */
	public void deleteFavoriteFromUsers(String name, String language) {
		
		List<User> users = userDao.getAllUsers();
		String temp = WordUtil.correctString(name)+" | "+language;
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
