package com.dicoloco.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.stereotype.Repository;

import com.dicoloco.constant.Identifiant;
import com.dicoloco.model.Word;

@Repository("dao")
public class WordDao {
	
	public WordDao() {
		
	}
	
	/**
	 * Methode findAllWords : recupere tous les mots de la bdd
	 * @return la liste des mots de la bdd
	 */
	public List<Word> findAllWords(){
		List <Word>listWords = new ArrayList<>();
		Connection myConn = null;
		Statement stmt = null;
		ResultSet myRs = null;
		try {
			
			Identifiant mySqlId = Identifiant.getInstance();
			myConn = (mySqlId.getConnection());
			stmt = myConn.createStatement();
			myRs = stmt.executeQuery("select * from word");
			
			while(myRs.next()) {
				
				List<String> synonyms = new ArrayList<>();
				StringTokenizer synonymsTokens = new StringTokenizer(myRs.getString("synonyms"),"_");
				
				while(synonymsTokens.hasMoreTokens()) {
					synonyms.add(synonymsTokens.nextToken());
				}
				
				List<String> definitions = new ArrayList<>();
				StringTokenizer definitionsTokens = new StringTokenizer(myRs.getString("definitions"),"_");
				
				while(definitionsTokens.hasMoreTokens()) {
					definitions.add(definitionsTokens.nextToken());
				}
				
				listWords.add(new Word(myRs.getString("name"), definitions, myRs.getString("gender"), 
						myRs.getString("category"),synonyms, myRs.getString("language")));
			}
			


		}catch(SQLException e) {
//			e.printStackTrace();
			System.out.println("Erreur SQL - WordDao.findAllWords");
		}catch(Exception e1) {
			System.out.println("Erreur de connexion à la base de donnees - UserDAO.findAllWords");
		}finally {
			try {
				stmt.close();
				myConn.close();
				myRs.close();
			} catch (SQLException e) {
				System.out.println("Erreur lors de la deconnexion à la base de donnees - WordDao.findAllWords");
			}
			
		}
		return listWords;
	}
	
	/**
	 * Methode findAllWordsWithLanguage : recupere tous les mots de la bdd selon la langue
	 * @param language la langue concernee
	 * @return la liste des mots de la bdd
	 */
	public List<Word> findAllWordsWithLanguage(String language){
		List <Word>listWords = new ArrayList<>();
		Connection myConn = null;
		Statement stmt = null;
		ResultSet myRs = null;

		try {
			
			Identifiant mySqlId = Identifiant.getInstance();
			
			myConn = (mySqlId.getConnection());
			stmt = myConn.createStatement();
			myRs = stmt.executeQuery("select * from word where language = '"+language+"'");
			
			while(myRs.next()) {
				
				List<String> synonyms = new ArrayList<>();
				StringTokenizer synonymsTokens = new StringTokenizer(myRs.getString("synonyms"),"_");
				
				while(synonymsTokens.hasMoreTokens()) {
					synonyms.add(synonymsTokens.nextToken());
				}
				
				List<String> definitions = new ArrayList<>();
				StringTokenizer definitionsTokens = new StringTokenizer(myRs.getString("definitions"),"_");
				
				while(definitionsTokens.hasMoreTokens()) {
					definitions.add(definitionsTokens.nextToken());
				}
				
				listWords.add(new Word(myRs.getString("name"), definitions, myRs.getString("gender"), 
						myRs.getString("category"),synonyms, myRs.getString("language")));
			}

		}catch(SQLException e) {
			System.out.println("Erreur SQL - UserDAO.findAllWordsWithLanguage");
		}catch(Exception e1) {
			System.out.println("Erreur de connexion à la base de donnees - UserDAO.findAllWordsWithLanguage");
		}finally {
			try {
				myConn.close();
				stmt.close();
				myRs.close();
			} catch (SQLException e) {
				System.out.println("Erreur lors de la deconnexion à la base de donnees - WordDao.findAllWordsWithLanguage");
			}
		}
		return listWords;
	}
	
	/**
	 * Methode findWord : cherche le mot correspondant
	 * @param wordName le nom du mot
	 * @param language la langue du mot
	 * @return le mot recherche ou null si pas trouve
	 */
	public Word findWord(String wordName, String language) {
		Word word = null;
		Connection myConn = null;
		Statement stmt = null;
		ResultSet myRs = null;
		try {
			
			Identifiant mySqlId = Identifiant.getInstance();
			StringBuffer sql = new StringBuffer();
			
			sql.append("select * from word where name='");
			sql.append(wordName);
			sql.append("' and language='");
			sql.append(language);
			sql.append("';");
			
			myConn = (mySqlId.getConnection());
			stmt = myConn.createStatement();
			myRs = stmt.executeQuery(sql.toString());
			
			while(myRs.next()) {
				
				List<String> synonyms = new ArrayList<>();
				StringTokenizer synonymsTokens = new StringTokenizer(myRs.getString("synonyms"),"_");
				
				while(synonymsTokens.hasMoreTokens()) {
					synonyms.add(synonymsTokens.nextToken());
				}
				
				List<String> definitions = new ArrayList<>();
				StringTokenizer definitionsTokens = new StringTokenizer(myRs.getString("definitions"),"_");
				
				while(definitionsTokens.hasMoreTokens()) {
					definitions.add(definitionsTokens.nextToken());
				}
				word = new Word(myRs.getString("name"), definitions, myRs.getString("gender"), 
						myRs.getString("category"), synonyms, myRs.getString("language"));
			}
			
		} catch(SQLException e) {
			//e.printStackTrace();
			System.out.println("Erreur SQL - WordDao.findWord");
		}catch(Exception e1) {
			System.out.println("Erreur de connexion à la base de donnees - UserDAO.findWord");
		}finally {
			try {
				myConn.close();
				stmt.close();
				myRs.close();
			} catch (SQLException e) {
				System.out.println("Erreur lors de la deconnexion à la base de donnees - WordDao.findWord");
			}
		}
		
		return word;
	}
	
	/**
	 * Methode updateWord : met a jour le mot dans la bdd	 
	 * @param wordName le nom du mot
	 * @param wordSynonyms les synonymes a ajouter
	 * @param language la langue du mot
	 */
	public void updateWord(String wordName, String wordSynonyms, String language) {
				
		Connection myConn = null;
		Statement stmt = null;
		try {
			
			Identifiant mySqlId = Identifiant.getInstance();
			StringBuffer sql = new StringBuffer();
			
			sql.append("update word set synonyms = '");
			sql.append(wordSynonyms.toString());
			sql.append("' where name = '");
			sql.append(wordName);
			sql.append("' and language='");
			sql.append(language);
			sql.append("'");
			
			myConn = (mySqlId.getConnection());
			stmt = myConn.createStatement();
			stmt.executeUpdate(sql.toString());
			
			
		}catch(SQLException e) {
			System.out.println("Erreur SQL - UserDAO.createWord");
		}catch(Exception e1) {
			System.out.println("Erreur de connexion à la base de donnees - UserDAO.createWord");
		}finally {
			try {
				myConn.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println("Erreur lors de la deconnexion à la base de donnees - WordDao.updateWord");
			}
		}
	}
	
	/**
	 * Methode deleteWord : supprime un mot de la bdd
	 * @param wordName le nom du mot
	 * @param language la langue du mot
	 * @return 0 si le mot a bien ete supprime, 1 si le mot n'a pas ete supprime
	 */
	public int deleteWord(String wordName, String language) {
			
		Connection myConn = null;
		Statement stmt = null;
		try {
			Identifiant mySqlId = new Identifiant();
			StringBuffer sql = new StringBuffer();
			
			sql.append("delete from word where name = '");
			sql.append(wordName);
			sql.append("' and language='");
			sql.append(language);
			sql.append("'");
			
			myConn = (mySqlId.getConnection());
			stmt = myConn.createStatement();
			stmt.executeUpdate(sql.toString());
			
			
		}catch(SQLException e) {
			System.out.println("Erreur SQL - UserDAO.createWord");
		}catch(Exception e1) {
			System.out.println("Erreur de connexion à la base de donnees - UserDAO.createWord");
		}finally {
			try {
				myConn.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println("Erreur lors de la deconnexion à la base de donnees - WordDao.deleteWord");
			}
		}
		
		if (findWord(wordName, language) == null) {
			return 0;
			
		} else {
			return 1;
		}
	}
	
	/**
	 * Methode createWord : creer un nouveau mot dans la bdd
	 * @param name le nom du mot
	 * @param definitions les definitions du mot
	 * @param gender le genre du mot
	 * @param category la categorie du mot
	 * @param synonyms les synonymes du mot
	 * @param language la langue du mot
	 */
	public void createWord(String name, String definitions, String gender, 
							String category, String synonyms, String language) {
		
		Connection myConn = null;
		Statement stmt = null;
		try {
			Identifiant mySqlId = new Identifiant();
			StringBuffer sql = new StringBuffer();
			
			sql.append("insert into word values ('");
			sql.append(name);
			sql.append("','");
			sql.append(definitions);
			sql.append("','");
			sql.append(gender);
			sql.append("','");
			sql.append(category);
			sql.append("','");
			sql.append(synonyms);
			sql.append("','");
			sql.append(language);
			sql.append("')");
			
			myConn = (mySqlId.getConnection());
			stmt = myConn.createStatement();
			stmt.executeUpdate(sql.toString());
			
		}catch(SQLException e) {
			System.out.println("Erreur SQL - UserDAO.createWord");
		}catch(Exception e1) {
			System.out.println("Erreur de connexion à la base de donnees - UserDAO.createWord");
		}finally {
			try {
				myConn.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println("Erreur lors de la deconnexion à la base de donnees - WordDao.createWord");
			}
		}
	}
	
	/**
	 * Methode createWordBDDFound : creer une liste de mots dans la bdd
	 * Regroupe les definitions en un string
	 * Chaque definition est separee par un _
	 * Pareil pour les synonymes
	 * @param words la liste de mots 
	 * @return 0 si tout va bien, 1 si echec de l'ajout
	 */
	public int createWordBDDFound(List<Word> words) {
		
		Connection myConn = null;
		Statement stmt = null;
		try {
			boolean continu = true;
			int count = 0;

			while(continu) {
				if(count<words.size()) {
					int array = words.size() - count;
					if(array<15000) {
						continu = false;
					}
					else {
						array=15000;
					}
					
					Identifiant mySqlId = new Identifiant();
					StringBuffer sql = new StringBuffer();
					sql.append("insert into word values ");
					
					for(int i = count; i<count+array;i++) {

						if(!words.get(i).getName().contains("/")) {
							StringBuffer definitions = new StringBuffer();

							for(int j=0; j<words.get(i).getDefinitions().size(); j++) {
								definitions.append(words.get(i).getDefinitions().get(j));
								definitions.append("_");
							}

							sql.append("('");
							sql.append(words.get(i).getName());
							sql.append("','");
							sql.append(definitions.toString());
							sql.append("','");
							sql.append("Genre a cote des definitions");
							sql.append("','");
							sql.append("Categorie a cote des definitions");
							sql.append("','");

							for(int k=0; k<words.get(i).getSynonyms().size();k++) {
								sql.append(words.get(i).getSynonyms().get(k));
								sql.append("_");
							}

							sql.append("','");
							sql.append(words.get(i).getLanguage());
							sql.append("')");

							if(i==count+array-1) {
								sql.append(";");
							}
							else {
								sql.append(",");
							}
						}	
					}
					
					count += array;
					System.out.println(sql.toString());
					myConn = (mySqlId.getConnection());
					stmt = myConn.createStatement();
					stmt.executeUpdate(sql.toString());	
					

				}
				else {
					continu = false;
				}
			}
			
		}catch(SQLException e) {
			System.out.println("Erreur SQL - UserDAO.createWordBDDFound");
			return 1;
		}catch(Exception e1) {
			System.out.println("Erreur de connexion à la base de donnees - UserDAO.findAllNames");
			return 1;
		}
		
		return 0;
	}
	
	/**
	 * Methode findAllNames : permet de recuperer tous les noms des mots de la bdd qui sont de la langue choisie
	 * @param language la langue des mots
	 * @return la liste de noms
	 */
	public List<String> findAllNames(String language) {
		List <String>listWords = new ArrayList<>();
		Connection myConn = null;
		Statement stmt = null;
		ResultSet myRs = null;
		try {
			
			Identifiant mySqlId = Identifiant.getInstance();
			myConn = (mySqlId.getConnection());
			stmt = myConn.createStatement();
			myRs = stmt.executeQuery("select name from word where language = '"+language+"'");
			while(myRs.next()) {	
				listWords.add(myRs.getString("name"));
			}
			

		}catch(SQLException e) {
			System.out.println("Erreur SQL - UserDAO.findAllNames");
		}catch(Exception e1) {
			System.out.println("Erreur de connexion à la base de donnees - UserDAO.findAllNames");
		}finally{
			try {
				myConn.close();
				stmt.close();
				myRs.close();
			} catch (SQLException e) {
				System.out.println("Erreur lors de la deconnexion à la base de donnees - WordDao.findAllNames");
			}
		}
		return listWords;
	}

}
