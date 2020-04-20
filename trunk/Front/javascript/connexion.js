/* Lien url vers l'API du Springboot */
var urlAPI = "http://localhost:8080";

/* Fonction pour la connexion utilisateur */
function loginAction() { 
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
        }
    };

	/* Recupere le nom entrer par utilisateur */
    var username = document.getElementById("username").value;

    if (username != "") {
        startLoading();
        /* Envoi de la requete HTTP */
        xhttp.open("GET", urlAPI + "/login/" +username, true);
        xhttp.responseType = 'json';
        xhttp.send();

        /* Recupere la reponse, un objet User sous format JSON et genere les modifications */
        xhttp.onload = function () {
            var user = xhttp.response;

            if (user != null) {
                alert("Succès : Utilisateur valide. Vous allez etre rediriger vers la page d'Accueil.")
                var size = taille(user);
                var beforeUser = localStorage.getItem('UserName');

                /* Vérifie si l'user se connecte à son 2ème compte sans s'etre deconnecte 
                 * Exemple : User se connecte à son compte 1, puis se connecte à son compte 2 sans avoir cliquer sur le bouton de Déconnexion avant 
                 */
                if (beforeUser != "") {
                    setUser(user);
                    redirectAccueilPage();
                    generateHtmlLoginNoLogout();
                }
                else {
                    setUser(user);
                    redirectAccueilPage();
                    generateHtmlLogin();
                }
                
            } else {
                alert("Erreur : le nom de cet utilisateur n'existe pas ! Veuillez entrer un nom valide.")
            }
        }
        endLoading();
    } else {
        alert("Connexion : Veuillez entrer un nom d'utilisateur");
    }
	
}

/* Genere le HTML selon l'utilisateur connecte de la page dicoloco_accueil.html
 * Exemple : User se connecte à son compte 1, puis se connecte à son compte 2 sans avoir cliquer sur le bouton de Déconnexion avant 
 */
function generateHtmlLogin() {
    /* Recupere les donnees de l'utilisateur qui se trouve dans le LocalStorage */
    var UserName = localStorage.getItem('UserName');
    var UserFavorites = localStorage.getItem('UserFavorites');

    if (UserName != null) {
    	document.querySelector("#Name").innerHTML = UserName;
        deconnexionGenerator();
        favoritesListInterfaceGenerator();
        favoritesGenerator();
    } 
}

/* Genere le HTML selon l'utilisateur connecte de la page dicoloco_accueil.html */
function generateHtmlLoginNoLogout() {
    /* Recupere les donnees de l'utilisateur qui se trouve dans le LocalStorage */
    var UserName = localStorage.getItem('UserName');
    var UserFavorites = localStorage.getItem('UserFavorites');

    if (UserName != null) {
        document.querySelector("#Name").innerHTML = UserName;
        favoritesListInterfaceGenerator();
        favoritesGenerator();
    } 
}

/* Genere les infos de l'utilisateur et le boutons de deconnexion
 * pour la navbar sur toutes les autres pages sauf dicoloco_accueil.html qui a sa propore fonction 
 */
function generateHtmlLoginOtherPage() {
    /* Recupere les donnees de l'utilisateur qui se trouve dans le LocalStorage */
    var UserName = localStorage.getItem('UserName');
    var UserFavorites = localStorage.getItem('UserFavorites');

    if (UserName != null) {
        document.querySelector("#Name").innerHTML = UserName;
        deconnexionGenerator();
    } 
}

/* Initialise le LocalStorage avec les infos de l'utilisateur pour les faire persister */
function setUser(userObject) {
	var inputUserName = userObject['name'];
    var inputUserFavorites = userObject['favorites'];

	localStorage.setItem('UserName', inputUserName);
	localStorage.setItem('UserFavorites', inputUserFavorites);
}

/* Genere le code html pour le bouton de Deconnexion sur la navbar */
function deconnexionGenerator(){
    const div = document.createElement('div');
    div.id = 'deconnexionGenerator';

    div.innerHTML = `<button type="button" class="btn btn-danger" onclick="logout()">Déconnexion</button>`;
    document.getElementById('deconnexion').appendChild(div);
}

/* Genere le code html pour la liste de favoris de l'utilisateur */
function favoritesGenerator(){
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
        }
    };

	/* Récupère le nom entrer par utilisateur */
    var username = localStorage.getItem('UserName');
    if (username != null) {
    	/*Envoi de la requete HTTP*/
    	xhttp.open("GET", urlAPI + "/login/" +username, true);
    	xhttp.responseType = 'json';
    	xhttp.send();
    	var user;	
        
    	xhttp.onload = function () {
    		user = xhttp.response;

    		var ula = document.getElementById('favoritesUl');

            if (ula != null) {
                /* Numerote le nombre de favoris */ 
                var numberOfFavorite = 0;
                /* Boucle pour generer la liste des favoris */
        		for(var i =0; i<taille(user.favorites); i++) {
        			
        			var lia = document.createElement('li');
        			ula.appendChild(lia);
        			lia.innerHTML = `<button class="btn btn-outline-secondary btn-sm" type="button" id="favToSearch`+numberOfFavorite+`" onClick="searchWord3(`+numberOfFavorite+`)">`+ user.favorites[i] +`</button>`;
        			numberOfFavorite++;
        		}
            }		
    	}
    }
}

/* Genere le code html pour la liste de favoris de l'utilisateur */
function favoritesListInterfaceGenerator(){
    var toHideSection = document.getElementById('ToHideLogin');
    toHideSection.remove();
}

/* Fonction de Deconnexion, efface les donnees persistent de l'utilisateur & le bouton Deconnexion et redirige vers l'Accueil */
function logout() {
	localStorage.removeItem('UserName');
	var toHide = document.getElementById('deconnexionGenerator');
    toHide.remove();
    alert("Déconnexion réussie !")
   	redirectAccueilPage();
}

/* Redirige vers la page Accueil */
function redirectAccueilPage() {
    document.location.href = "dicoloco_accueil.html";
}

function taille(obj) {
	var size = 0;
	while(obj[size]!=null){
		size++;
	}
	return size;
}

/* Fonction pour supprimer un user de la bdd */
function deleteUser() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            // alert(this.responseText);
        }
    };

    var UserDelete = document.getElementById("UserDelete").value;

    if (UserDelete != "") {
        startLoading();
        xhttp.open("GET", urlAPI + "/delete/" + UserDelete, true);
        xhttp.setRequestHeader("Content-type", "application/json");
        xhttp.send();

        /* Recupere la reponse, un objet User sous format JSON et genere les messages de reponses */
        xhttp.onload = function () {
            var response = xhttp.response;

            if (response == 0) {
                alert("Succès : L'utilisateur '" + UserDelete + "' a bien été supprimé de la bdd.");
                logout();
            } else if (response == 1) {
                alert("Erreur : L'utilisateur '" + UserDelete + "' n'a pas été supprimé de la bdd !");
            } else if (response == 2) {
                alert("Erreur : L'utilisateur '" + UserDelete + "' n'existe pas dans la bdd !");
            } else {
                alert("Erreur : La fonctionnalité Supprimer rencontre actuellement un problème technique, Veuillez nous contacter !");
            }
        }
        endLoading();
    } else {
        alert("Suppression du compte utilisateur : Veuillez entrer votre pseudo")
    }
}