# Dicoloco

Le dictionnaire en ligne de demain. 


## Présentation 

Ceci est un projet informatique universitaire (2019-2020) dans le contexte de l'UE Projet fait par un groupe de 4 étudiants en L3 MIAGE à l'Université de Paris (anciennement Université Paris-Descartes) avec la participation et l'encadrement de M. LHOUMEAU Maxime.

DicoLoco est une application web destinée au grand public, qui propose de mettre à leurs disposition toutes les fonctionnalités d'un dictionnaire et bien plus encore (à venir). 

Fonctionnalités : 
 - Rechercher la définition d'un mot 
 - Afficher une suggestion de mots proches lorsque le mot est introuvable dans la base de données 
 - Ajouter un mot ou une liste de mots(via fichiers JSON) dans la base de données 
 - Modifier les synonymes d'un mot 
 - Inscription/Connexion à un compte utilisateur permettant l'ajout de mot dans sa liste de Favoris 


## Pré requis 

MySQL : Pour la base de données 
Eclipse avec l'extension Spring Tool Suite ou IDE Spring Tool Suite : Pour la partie Back-end de l'application web 
Google Chrome ou Microsoft Edge : Pour le navigateur web 


## Installation

1) La base de donnée MySQL 
 - Lancez MySQL 
 - Configurez une nouvelle connexion (retenez votre identifiant et mot de passe choisis lors de la configuration)

Une fois la configuration terminée, 
 - Créez une database en retenant le nom choisi pour celle-ci 

Ensuite, aller à (Projet_L3AY1\branches\sql\dicoloco.sql) : 
 - Ouvrez le fichier 
 - Copiez le contenu, Collez-le et lancer le script SQL sur la database crée sur MySQL

2) Le Back-end sur Spring Tool Suite 

Sur Spring Tool Suite, 
 - Importez le dossier Projet_L3AY1 en faisant un "Import" -> "Existing Maven Project"

Une fois le projet récupérer sur l'IDE,
 - Allez à (Projet_L3AY1\branches\SpringBoot\project_v1\src\main\java\com\dicoloco\constant\Identifiant.java)
 - Modifiez la valeur des attributs "id" (identifiant MySQL), "password" (mot de passe MySQL) et "dbname" (nom de la database MySQL) selon votre cas 
 - Allez à (Projet_L3AY1\branches\SpringBoot\project_v1\src\main\resources\application.properties) 
 - Modifiez la valeur des "spring.datasource.username" (identifiant MySQL), "spring.datasource.password" (mot de passe MySQL)
 - Modifiez la valeur de "spring.datasource.url" avec "jdbc:mysql://localhost:3306/" + Nom de la database MySQL + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC" (sans espace sur une seule ligne)

L'installation du projet est terminée. 


## Démarrage 

1) Le Back-end sur Spring Tool Suite 
 - Puis, allez à (Projet_L3AY1\branches\SpringBoot\project_v1\src\main\java\com\dicoloco\DicolocoApplication.java)
 - Et lancez le projet

2) L'interface client de l'applcation web
 - Allez à (Projet_L3AY1\branches\Front\html\dicoloco.html)
 - Ouvrez le fichier sur un navigateur web

Le démarrage du projet est terminé. 
 

## Usage

Pour le moment, réservé à usage éducatif et non lucratif.  


## Version(s)

Version 1.0 (stable)


## Auteurs

Encadrant MOA : 
 - M. LHOUMEAU Maxime 

Etudiants MOE (Développeurs Back-end & Front-end) : 
 - DAI William 
 - KITKIT Hasna 
 - LE Marcel 
 - YU Willy 


## Licence(s)

Pour le moment, réservé uniquement aux auteurs du projet et à l'Université de Paris dans le cas de l'UE Projet de L3 MIAGE (2019/2020).