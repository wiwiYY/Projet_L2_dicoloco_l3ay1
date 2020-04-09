package com.dicoloco.controller;
import java.util.List;

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
    
    /**
     * Recupere les informations d'un utilisateur
     * @param name
     * @param model
     * @return La page avec les informations de l'utilisateur
     */
    @GetMapping(value ="/login/{name}")
	public User getUserPage(@PathVariable(name="name") String name) {
    	UserService service = new UserService();
        User user = service.findUserAccount(name);
        return user;
    }
    
    /**
     * Recupere la liste d'utilisateur
     * @return la liste d'utilisateurs
     */
    @RequestMapping(value = "/getUsers", method = RequestMethod.GET)
    public List<User> getUsers(){
    	UserService w = new UserService();
		return w.findAllUsers();
    }
    
    /**
     * Ajout d'un nouvel utilisateur 
     * @param name
     */
    @GetMapping("/create/{name}")
	@ResponseBody
	public User createUserController(@PathVariable("name") String name) {
		UserService u = new UserService();
		if(u.createUser(name) == 1) {
			return u.findUserAccount(name);
		}
		return null;
	}
    
    /**
     * Met a jour la liste de favoris d'un utilisateur 
     * @param word Mot a mettre a jour
     * @param username Compte de l'utilisateur en question
     * @param method Add ou Delete
     */
    @GetMapping("/updateFavorites/{word}/{username}/{method}")
	@ResponseBody
	public int addFavoriteController(@PathVariable(name="word") String name, 
			@PathVariable(name="username") String username, @PathVariable (name="method") String method) {
		UserService u = new UserService();
		return u.updateFavorites(name, username, method);
	}
}