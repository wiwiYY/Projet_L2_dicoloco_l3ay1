package com.dicoloco.dao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	 * Methode getAllUsers : Recherche tous les utilisateurs de la base de donnee
	 * @return la liste d'utilisateurs trouve
	 */
	public List<User> getAllUsers(){

		List <User>listUsers = new ArrayList<>();
		Identifiant mySqlId = new Identifiant();
		Connection myConn = null;
		Statement stmt = null;

		try {
			myConn = mySqlId.getConnection();
			stmt = myConn.createStatement();
			ResultSet myRs = stmt.executeQuery("select * from user");

			while(myRs.next()) {
				List<String> favorites = new ArrayList<>();
				StringTokenizer favoritesTokens = new StringTokenizer(myRs.getString("favorites"),"_");

				while(favoritesTokens.hasMoreTokens()) {
					favorites.add(favoritesTokens.nextToken());
				}

				listUsers.add(new User(myRs.getString("name"), favorites));
			}	
		}catch(Exception e) {
			System.out.println("Erreur de connexion à la base de donnees - UserDAO.getAllUsers");
		}finally {
			try {
				myConn.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println("Erreur lors de la deconnexion à la base de donnees - UserDAO.getAllUsers");
			}
		}
		return listUsers;
	}
	/**
	 * Methode finUserAccount : Cherche les information de l'utilisateur
	 * @param userName Nom de l'utilisateur 
	 * @return User les information de l'utilisateur ou bien null s'il ne la pas trouve
	 */
	public User findUserAccount(String userName) {

		ResultSet myRs = null;
		User user = null;
		Connection myConn = null;
		Statement stmt = null;

		try {
			StringBuffer sql = new StringBuffer();

			sql.append("select * from user where name = '");
			sql.append(userName);
			sql.append("'");

			Identifiant mySqlId = new Identifiant();
			myConn = mySqlId.getConnection();
			stmt = myConn.createStatement();
			myRs = stmt.executeQuery(sql.toString());

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
			System.out.println("Erreur de connexion à la base de donnees - UserDAO.findUserAccount");
		}finally {
			try {
				myConn.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println("Erreur lors de la deconnexion à la base de donnees - UserDAO.findUserAccount");
			}
		}
		return user;
	}
		

	/**
	 * Methode createUser : Creer un nouvel utilisateur
	 * @param name Nom du nouvel utilisateur a cree
	 */
	public void createUser(String name) {
		
		Connection myConn = null;
		Statement stmt = null;
		try {  
			Identifiant mySqlId = new Identifiant();
			StringBuffer sql = new StringBuffer();

			sql.append("insert into user values ('");
			sql.append(name);
			sql.append("','')");

			myConn = mySqlId.getConnection();
			stmt = myConn.createStatement();
			stmt.executeUpdate(sql.toString());
		}catch(Exception e) {
			System.out.println("Erreur de connexion à la base de donnees - UserDAO.createUser");
		}finally {
			try {
				myConn.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println("Erreur connexion lors de la deconnexion à la base de donnees - UserDAO.createUser");
			}
		}
	}

	/**
	 * Methode updateFavoritesList : Met a jour la liste de favoris d'un utilisateur 
	 * @param userName Nom de l'utilisateur
	 * @param favoritesList Liste de Favoris de l'utilisateur 
	 */
	public void updateFavoritesList(String userName, String favoritesList) {

		Connection myConn = null;
		Statement stmt = null;
		try {
			Identifiant mySqlId = Identifiant.getInstance();
			StringBuffer sql = new StringBuffer();

			sql.append("update user set favorites = '");
			sql.append(favoritesList.toString());
			sql.append("' where name = '");
			sql.append(userName);
			sql.append("'");

			myConn = mySqlId.getConnection();
			stmt = myConn.createStatement();
			stmt.executeUpdate(sql.toString());
		}catch(Exception e) {
			System.out.println("Erreur de connexion à la base de donnees - UserDAO.updateFavoritesList");
		}finally {
			try {
				myConn.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println("Erreur lors de la deconnexion à la base de donnees - UserDAO.updateFavoritesList");
			}
		}
	}	
	
	/**
	 * Methode deleteUser : Supprime un utilisateur de la base de donnee
	 * Retourne 0 si l'utilisateur a bien ete supprime 
	 * Retourne 1 si l'utilisateur n'a pas ete supprime 
	 * @param user le nom de l'utilisateur a supprimer
	 * @return int Reponse de retour de la methode
	 */
	public int deleteUser(String user) {
		
		Connection myConn = null;
		Statement stmt = null;
		try {
			Identifiant mySqlId = new Identifiant();
			StringBuffer sql = new StringBuffer();
			
			sql.append("delete from user where name = '");
			sql.append(user);
			sql.append("'");
			
			myConn = mySqlId.getConnection();
			stmt = myConn.createStatement();
			stmt.executeUpdate(sql.toString());

		}catch(Exception e) {
			System.out.println("Erreur de connexion à la base de donnees - UserDAO.deleteUser");
		}finally {
			try {
				myConn.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println("Erreur lors de la deconnexion à la base de donnees - UserDAO.deleteUser");
			}
		}
		
		if (findUserAccount(user) == null) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * Methode removeAllUsers : Supprime tous les utilisateurs de la base de donnee
	 * Attention methode sans retour
	 */
	public void removeAllUsers() {
		Connection myConn = null;
		Statement stmt = null;
		try {
			Identifiant mySqlId = new Identifiant();
			StringBuffer sql = new StringBuffer();
			
			sql.append("delete from user");
			
			myConn = mySqlId.getConnection();
			stmt = myConn.createStatement();
			stmt.executeUpdate(sql.toString());

		}catch(Exception e) {
			System.out.println("Erreur de connexion à la base de donnees - UserDAO.removeAllUsers");
		}finally {
			try {
				myConn.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println("Erreur lors de la deconnexion à la base de donnees - UserDAO.removeAllUsers");
			}
		}
	
	
	}


	/**
	 * Methode addUsers : ajoute une liste d'utilisateur
	 * @param users listes contenant les informations de chaque utilisateur a ajouter
	 */
	public void addUsers(List<User> users) {
		Connection myConn = null;
		Statement stmt = null;
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

			myConn = mySqlId.getConnection();
			stmt = myConn.createStatement();
			stmt.executeUpdate(sql.toString());
		}catch(Exception e) {
			System.out.println("Erreur de connexion à la base de donnees - UserDAO.addUsers");
		}finally {
			try {
				myConn.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println("Erreur lors de la deconnexion à la base de donnees - UserDAO.addUsers");
			}
		}
	}
}