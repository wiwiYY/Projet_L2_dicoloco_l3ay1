package com.dicoloco.dao;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.dicoloco.constant.Identifiant;
import com.dicoloco.model.User;

@Repository
@Transactional
public class UserDAO {

	/**
	 * Retourne la liste des utilisateurs
	 * @return Liste d'utilisateur
	 */
	public List<User> getAllUsers(){

		List <User>listUsers = new ArrayList<>();

		try {
			Identifiant mySqlId = new Identifiant();
			ResultSet myRs = (mySqlId.getStatement()).executeQuery("select * from user");

			while(myRs.next()) {
				List<String> favorites = new ArrayList<>();
				StringTokenizer favoritesTokens = new StringTokenizer(myRs.getString("favorites"),"_");

				while(favoritesTokens.hasMoreTokens()) {
					favorites.add(favoritesTokens.nextToken());
				}

				listUsers.add(new User(myRs.getString("name"), favorites));
			}	
		}catch(Exception e) {
			e.printStackTrace();
		}

		return listUsers;
	}

	/**
	 * Cherche l'utilisateur correspondant
	 * @param userName Nom de l'utilisateur 
	 * @return L'utilisateur
	 */
	public User findUserAccount(String userName) {

		ResultSet myRs = null;
		User user = null;

		try {
			StringBuffer sql = new StringBuffer();

			sql.append("select * from user where name = '");
			sql.append(userName);
			sql.append("'");

			Identifiant mySqlId = new Identifiant();
			myRs = (mySqlId.getStatement()).executeQuery(sql.toString());

			while(myRs.next()) {
				List<String> favorites = new ArrayList<>();
				StringTokenizer favoritesTokens = new StringTokenizer(myRs.getString("favorites"),"_");

				while(favoritesTokens.hasMoreTokens()) {
					favorites.add(favoritesTokens.nextToken());
				}

				user = new User(myRs.getString("name"), favorites);
				System.out.println("Name : "+myRs.getString("name")+" , Favorites : "+ myRs.getString("favorites"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

		return user;
	}

	/**
	 * Creer un nouvel utilisateur 
	 * @param name Nom du nouvel utilisateur
	 */
	public void createUser(String name) {

		try {  
			Identifiant mySqlId = new Identifiant();
			StringBuffer sql = new StringBuffer();

			sql.append("insert into user values ('");
			sql.append(name);
			sql.append("','')");

			(mySqlId.getStatement()).executeUpdate(sql.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update la liste de favoris d'un utilisateur dans la bdd	 
	 * @param userName Nom de l'utilisateur
	 * @param favoritesList Liste de Favoris de l'utilisateur 
	 */
	public void updateFavoritesList(String userName, String favoritesList) {

		try {
			Identifiant mySqlId = Identifiant.getInstance();
			StringBuffer sql = new StringBuffer();

			sql.append("update user set favorites = '");
			sql.append(favoritesList.toString());
			sql.append("' where name = '");
			sql.append(userName);
			sql.append("'");

			(mySqlId.getStatement()).executeUpdate(sql.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}	
}