package com.dicoloco.constant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//import org.springframework.beans.factory.annotation.Value;

/**
 * Classe de configuration de la DataBase MySQL
 */
public class Identifiant {
	
	//l'id de MySQL
//	private String id = "projectl3";
//	private String id = "root";
	private static String id;
	
	//Mdp de MySQL
//	private String password = "root1234";
//	private String password = "admin";
	private static String password;

	//Nom de la DataBase de MySQL
//	private String dbname = "databasel3";
	private String dbname = "word";
	
	//Url de la bdd
//	private String url = "db4free.net:3306/";
	private String url = "localhost:3306/";
		
	public String getDb() {
		return dbname;
	}

	public void setDb(String db) {
		this.dbname = db;
	}

	private static Identifiant INSTANCE;
	
	public Identifiant() {
		
	}
	
	public static Identifiant getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Identifiant();
		}
		
		return INSTANCE;
	}
	
	public String getId() {
		return id;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setId(String newid) {
		id = newid;
	}
	
	public void setPassword(String newPassword) {
		password = newPassword;
	}
	/**
	 * Methode getConnection : permet de recuperer une connection avec mySQL
	 * @return une connection
	 */
	public Connection getConnection() {
		Connection myConn;
		try {
			//myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+ dbname +"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", this.id, this.password);
			myConn = DriverManager.getConnection("jdbc:mysql://"+url+ dbname +"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", this.id, this.password);

			return myConn;
		} catch (SQLException e) {
			System.out.println("Erreur connection échoué, vérifier la méthode Identifiant.getConnection");
		} 
		return null;
		
	}
	
}

