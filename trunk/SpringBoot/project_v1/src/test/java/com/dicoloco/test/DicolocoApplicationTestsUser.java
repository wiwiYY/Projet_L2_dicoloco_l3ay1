package com.dicoloco.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.dicoloco.service.UserService;


/**
 * Pour lancer ce test, il faut actualiser les données
 * sur mySQL > relancer le sql
 * @author willy
 */

@SpringBootTest
class DicolocoApplicationTestsUser {
	
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
		assertEquals(1, userService.updateFavorites("Accessory", userName, method));
	}
	
	@Test
	public void userNotExistTest() {
		UserService userService = new UserService();
		String falseUser = "barry";
		String method = "add";
		assertEquals(2, userService.updateFavorites("a", falseUser, method));

	}
	
	
	@Test
	public void userDeleteFavouriteTest() {
		UserService userService = new UserService();
		String wordFavoris = "Abdominals";
		String userName = "toto";
		String method = "delete";
		assertEquals(1, userService.updateFavorites(wordFavoris, userName, method));
	}
	
	@Test
	public void userDeleteFavouriteNotFoundTest() {
		UserService userService = new UserService();
		String wordFavoris = "Aeronaut";
		String userName = "toto";
		String method = "delete";
		assertEquals(3, userService.updateFavorites(wordFavoris, userName, method));
	}
	
	@Test
	public void userAddFavouriteDoesAlreadyExistTest4() {
		UserService userService = new UserService();
		String userName = "toto";
		String method = "add";
		assertEquals(4, userService.updateFavorites("Accessory", userName, method));

	}
}
