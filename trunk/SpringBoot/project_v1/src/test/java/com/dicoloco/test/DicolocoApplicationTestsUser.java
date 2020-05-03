package com.dicoloco.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.dicoloco.model.User;
import com.dicoloco.service.UserService;
import com.dicoloco.service.WordService;
import com.dicoloco.dao.UserDAO;
import com.dicoloco.dao.WordDao;


/**
 * Pour lancer ce test, il faut actualiser les données
 * sur mySQL > relancer le sql
 * @author willy
 */

@SpringBootTest
class DicolocoApplicationTestsUser {
	
	@Autowired
	private UserService userService;
	
	@MockBean
	private UserDAO userDAO;
	
	@Test
	public void findUserByNameTest() {
		UserService userService = new UserService();
		String userName = "toto";
		assertNotNull(userService.findUserAccount(userName));	
	}
	
	@Test
	public void userAddFavouriteTest() {
		UserService userService = new UserService();
		String userName = "toto";
		String method = "add";
		
		//TODO : donnez un nouveau favoris pour chaque test a cet utilisateur
		assertEquals(1, userService.updateFavorites("Accessory","en", userName, method));
	}
	
	@Test
	public void userNotExistTest() {
		UserService userService = new UserService();
		String falseUser = "barry";
		String method = "add";
		assertEquals(2, userService.updateFavorites("Cosmologie","fr", falseUser, method));

	}
	
	
	@Test
	public void userDeleteFavouriteTest() {
		UserService userService = new UserService();
		String wordFavoris = "Abdominals";
		String userName = "toto";
		String method = "delete";
		
		//TODO : donnez un favoris existant dans la liste pour chaque test
		assertEquals(1, userService.updateFavorites(wordFavoris,"en", userName, method));
	}
	
	@Test
	public void userDeleteFavouriteNotFoundTest() {
		UserService userService = new UserService();
		String wordFavoris = "Aeronaut";
		String userName = "toto";
		String method = "delete";
		
		assertEquals(3, userService.updateFavorites(wordFavoris,"en", userName, method));
	}
	
	@Test
	public void userAddFavouriteDoesAlreadyExistTest4() {
		UserService userService = new UserService();
		String userName = "toto";
		String method = "add";
		
		assertEquals(4, userService.updateFavorites("Accessory","en", userName, method));

	}
	
	@Test
	public void findAllUsersTest() {
		UserService userService = new UserService();
		int numberUsers = userService.findAllUsers().size();
		
		//TODO : entrez un utilisateur qui n'existe pas dans la base de donnee
		userService.createUser("user1");
		userService.createUser("user2");

		assertEquals(numberUsers+2, userService.findAllUsers().size());
		
	}
	
	@Test
	public void deleteUserServiceTest() {
		UserService userService = new UserService();
		int numberUsers = userService.findAllUsers().size();
		
		//TODO : entrez un utilisateur qui est dans la base de donnee
		userService.deleteUserService("user1");
		
		assertEquals(numberUsers-1, userService.findAllUsers().size());
	}
}
