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
 - Installer puis lancer MySQL Installer
 - Choisissez l'option Developper default puis cliquer sur execute. Cela va installer des modules nécessaires
 - Les modules comme MySQL for Excel ou MySQL for Visual Studio installables manuellement ne sont pas nécessaires.
 - High Availability : choisissez Standalone MySQL Server / Classic MySQL Replication 
 - Type and Networking : ne modifiez rien 
 - Authentification Method : choisissez Use Strong Password Encryption for Authentification
 - Accounts and Roles : Pour le mot de passe, choisissez le mot de passe 'admin'. Pas besoin de créer des mySQL User Accounts
 - Windows Service : ne modifiez rien
 - Apply Configuration : execute
 - Product Configuration : execute
 - MySQL Router Configuration : ne modifiez rien. Finish
 - Connect To Server : En bas, tapez pour User Name 'root' et pour Password 'admin' puis cliquez sur Check
 - Lancez MySQL 
 - Configurez une nouvelle connexion (retenez votre identifiant et mot de passe choisis lors de la configuration)
 - Lancer le script dicoloco.sql

Une fois la configuration terminée, 
 - Créez une database en retenant le nom choisi pour celle-ci 

Ensuite, aller à (Projet_L3AY1\branches\sql\dicoloco.sql) : 
 - Ouvrez le fichier 
 - Copiez le contenu, Collez-le et lancer le script SQL sur la database crée sur MySQL

2) Le Back-end 
 - Installer Java JDK
 - Lancer le fichier .jar


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