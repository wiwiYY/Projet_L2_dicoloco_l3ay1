package com.dicoloco.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
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
	 * Recupere tous les mots de la bdd
	 * @return la liste des mots de la bdd
	 */
	public List<Word> findAllWords(){
		List <Word>listWords = new ArrayList<>();

		try {
			
			Identifiant mySqlId = Identifiant.getInstance();
			ResultSet myRs = (mySqlId.getStatement()).executeQuery("select * from word");
			
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

		}catch(Exception e) {
			e.printStackTrace();
		}
		return listWords;
	}
	
	/**
	 * Cherche le mot correspondant
	 * @param wordName
	 * @return le mot recherche ou null si pas trouve
	 */
	public Word findWord(String wordName) {
		Word word = null;
		try {
			
			Identifiant mySqlId = Identifiant.getInstance();
			StringBuffer sql = new StringBuffer();
			
			sql.append("select * from word where name='");
			sql.append(wordName);
			sql.append("'");
			
			ResultSet myRs = (mySqlId.getStatement()).executeQuery(sql.toString());
			
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return word;
	}
	
	/**
	 * Update le mot dans la bdd	 
	 * @param wordName
	 * @param wordSynonyms
	 * @param method 
	 */
	public void updateWord(String wordName, String wordSynonyms) {
				
		try {
			
			Identifiant mySqlId = Identifiant.getInstance();
			StringBuffer sql = new StringBuffer();
			
			sql.append("update word set synonyms = '");
			sql.append(wordSynonyms.toString());
			sql.append("' where name = '");
			sql.append(wordName);
			sql.append("'");
			
			(mySqlId.getStatement()).executeUpdate(sql.toString());

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Supprime un mot de la bdd
	 * Retourne 0 si le mot a bien été supprimé 
	 * Retourne 1 si le mot n'a âs été supprimé 
	 * @param wordName
	 * @return  int Réponse de retour de la méthode
	 */
	public int deleteWord(String wordName) {
				
		try {
			
			Identifiant mySqlId = new Identifiant();
			StringBuffer sql = new StringBuffer();
			
			sql.append("delete from word where name = '");
			sql.append(wordName);
			sql.append("'");
			
			(mySqlId.getStatement()).executeUpdate(sql.toString());

		}catch(Exception e) {
			e.printStackTrace();
		}
		
		if (findWord(wordName) == null) {
			return 0;
			
		} else {
			return 1;
		}
	}
	
	/**
	 * Créer un nouveau mot dans la bdd
	 * @param name
	 * @param definitions 
	 * @param gender
	 * @param category
	 * @param synonyms 
	 * @param language 
	 */
	public void createWord(String name, String definitions, String gender, 
							String category, String synonyms, String language) {
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

			(mySqlId.getStatement()).executeUpdate(sql.toString());
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Cree un mot depuis un fichier 
	 * Met les definitions en un string
	 * Chaque definition est separee par un _
	 * Pareil pour les synonymes
	 * @param words : liste de mots filtre depuis le fichier json
	 */
	public void createWordBDDFound(List<Word> words) {
		try {
			if(words.size()>0) {
				
				Identifiant mySqlId = new Identifiant();
				StringBuffer sql = new StringBuffer();
				sql.append("insert into word values ");
								
				for(int i=0; i<words.size(); i++ ) {
					
					if(!words.get(i).getName().contains("/")) {
						StringBuffer definitions = new StringBuffer();
						
						for(int j=0; j<words.get(i).getDefinition().size(); j++) {
							definitions.append(words.get(i).getDefinition().get(j));
							definitions.append("_");
						}
						
						sql.append("('");
						sql.append(words.get(i).getName());
						sql.append("','");
						sql.append(definitions.toString());
						sql.append("','");
						sql.append("Pas de genre");
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
						
						if(i==words.size()-1) {
						}
						else {
							sql.append(",");
						}
					}	
				}
				(mySqlId.getStatement()).executeUpdate(sql.toString());	
			}
		}catch(Exception e) {
			e.printStackTrace();
		}		
	}
}
