package com.dicoloco.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dicoloco.model.User;
import com.dicoloco.service.UserService;

@RestController
@RequestMapping(path="/")
public class UserController {
	
	private UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService; 
	}
    
    /**
     * Methode getUserInfo : Recupere les informations d'un utilisateur dans la base de donnee
     * @param name le nom de l'utilisateur
     * @return User les informations de l'utilisateur 
     */
    @GetMapping(value ="/login/{name}")
	public User getUserInfo(@PathVariable(name="name") String name) {
    	User user = userService.findUserAccount(name);
        return user;
    }
    
    /**
     * Methode getUsers : Recupere la liste des utilisateurs dans la base de donnee
     * @return la liste des utilisateurs retrouve
     */
    @RequestMapping(value = "/getUsers", method = RequestMethod.GET)
    public List<User> getUsers(){
		return userService.findAllUsers();
    }
    
    /**
     * Methode createUser : Creation d'un nouvel utilisateur avec le nom en parametre
     * @param name le nom du nouvelle utilisateur
     * @return User les informations de l'utilisateur creer
     */
    @GetMapping("/create/{name}")
	@ResponseBody
	public int createUser(@PathVariable("name") String name) {
		return userService.createUser(name);
	}
    
    /**
     * Methode addFavoriteController : Met a jour la liste de favoris d'un utilisateur 
     * @param word Mot a mettre a jour
     * @param language Langue choisie
     * @param username Utilisateur en question
     * @param method Add ou Delete
     */
    @GetMapping("/updateFavorites/{word}/{language}/{username}/{method}")
	@ResponseBody
	public int addFavoriteController(@PathVariable(name="word") String word, @PathVariable(name="language") String language, 
			@PathVariable(name="username") String username, @PathVariable (name="method") String method) {
		return userService.updateFavorites(word, language, username, method);
	}
    
    /**
	 * Methode deleteUser : Supprime un utilisateur de la base de donnee
	 * Retourne 0 si le user a bien ete supprime 
	 * Retourne 1 si le user n'a pas ete supprime 
	 * Retourne 2 si le user a supprimer n'existe pas 
	 * @param user le nom de l'utilisateur a supprimer
	 * @return int Reponse de retour de la methode 
	 */
	@GetMapping(value= "delete/{user}")
	public int deleteUser(@PathVariable(name="user") String user) {
		return userService.deleteUserService(user);
	}
	
	
}