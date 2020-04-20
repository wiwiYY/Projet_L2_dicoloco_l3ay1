package com.dicoloco.dao;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.dicoloco.constant.Identifiant;
import com.dicoloco.model.User;

@Repository("daoUser")
@Transactional
public class UserDAO {

	/**
	 * Retourne une liste contenant les utilisateurs 
	 * @return List<User> liste d'utilisateurs
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
	 * Cherche l'utilisateur correspondant au nom
	 * @param userName Nom de l'utilisateur 
	 * @return User Un utilisateur ou bien null
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
	 * Met a jour la liste de favoris d'un utilisateur 
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
	
	/**
	 * Supprime un user de la bdd
	 * Retourne 0 si le user a bien ete supprime 
	 * Retourne 1 si le user n'a pas ete supprime 
	 * @param user  
	 * @return int Reponse de retour de la methode
	 */
	public int deleteUser(String user) {
				
		try {
			Identifiant mySqlId = new Identifiant();
			StringBuffer sql = new StringBuffer();
			
			sql.append("delete from user where name = '");
			sql.append(user);
			sql.append("'");
			
			(mySqlId.getStatement()).executeUpdate(sql.toString());

		}catch(Exception e) {
			e.printStackTrace();
		}
		
		if (findUserAccount(user) == null) {
			return 0;
		} else {
			return 1;
		}
	}

	public void removeAllUsers() {
		try {
			Identifiant mySqlId = new Identifiant();
			StringBuffer sql = new StringBuffer();
			
			sql.append("delete from user");
			
			(mySqlId.getStatement()).executeUpdate(sql.toString());

		}catch(Exception e) {
			e.printStackTrace();
		}
	
	}

	public void addUsers(List<User> users) {
		try {  
			Identifiant mySqlId = new Identifiant();
			StringBuffer sql = new StringBuffer();

			sql.append("insert into user values ");
			for(int i=0;i<users.size();i++) {
				StringBuffer favorites = new StringBuffer();
				for(int j=0;j<users.get(i).getFavorites().size();j++) {
					favorites.append(users.get(i).getFavorites().get(j));
					favorites.append("_");
				}
				
				sql.append("('");
				sql.append(users.get(i).getName());
				sql.append("', '");
				sql.append(favorites.toString());
				sql.append("')");
				if(i<users.size()-1) {
					sql.append(",");
				}
				else {
					sql.append(";");
				}
			}

			(mySqlId.getStatement()).executeUpdate(sql.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}