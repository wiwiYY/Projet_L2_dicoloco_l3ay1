/* Fonction pour la connexion utilisateur */
function loginAction() { 
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
        }
    };

	/* Récupère le nom entrer par utilisateur */
    var username = document.getElementById("username").value;

    if (username != "") {
        /* Envoi de la requete HTTP */
        xhttp.open("GET", "http://localhost:8080/login/" +username, true);
        xhttp.responseType = 'json';
        xhttp.send();

        /* Récupère la réponse, un objet User sous format JSON et génère les modifications */
        xhttp.onload = function () {
            var user = xhttp.response;

            if (user != null) {
                alert("Succès : Utilisateur valide. Vous allez etre rediriger vers la page d'Accueil.")
                var size = taille(user);
                setUser(user);
                redirectAccueilPage();
                generateHtmlLogin();
            } else {
                alert("Erreur : le nom de cet utilisateur n'existe pas ! Veuillez entrer un nom valide.")
            }
        }
    } else {
        alert("Connexion : Veuillez entrer un nom d'utilisateur");
    }
	
}

/* Génère le HTML selon l'utilisateur */
function generateHtmlLogin() {
    /* Récupère les données de l'utilisateur qui se trouve dans le LocalStorage */
    var UserName = localStorage.getItem('UserName');
    var UserFavorites = localStorage.getItem('UserFavorites');

    if (UserName != null) {
    	document.querySelector("#Name").innerHTML = UserName;
        deconnexionGenerator();
        favoritesListInterfaceGenerator();
        favoritesGenerator();
    } 
}

/* Initialise le LocalStorage avec les infos de l'utilisateur pour les faire persister */
function setUser(userObject) {
	var inputUserName = userObject['name'];
    var inputUserFavorites = userObject['favorites'];

	localStorage.setItem('UserName', inputUserName);
	localStorage.setItem('UserFavorites', inputUserFavorites);
}

/* Génère le code html pour le bouton de Deconnexion */
function deconnexionGenerator(){
    const div = document.createElement('div');
    div.id = 'deconnexionGenerator';

    div.innerHTML = `<button type="button" class="btn btn-outline-danger" onclick="logout()">Déconnexion</button>`;
    document.getElementById('deconnexion').appendChild(div);
}

/* Génère le code html pour la liste de favoris de l'utilisateur */
function favoritesGenerator(){
    var ul = document.createElement('ul');
	//var UserFavorites = localStorage.getItem('UserFavorites');
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
        }
    };
	var numberOfFavorite = 0;
	/* Récupère le nom entrer par utilisateur */
    var username = localStorage.getItem('UserName');
    if (username != null) {
    	/*Envoi de la requete HTTP*/
    	xhttp.open("GET", "http://localhost:8080/login/" +username, true);
    	xhttp.responseType = 'json';
    	xhttp.send();
    	var user;
    	
        //boucle pour générer la liste des favoris
    	xhttp.onload = function () {
    		user = xhttp.response;

    		var ula = document.getElementById('favoritesUl');

            if (ula != null) {
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

/* Génère le code html pour la liste de favoris de l'utilisateur */
function favoritesListInterfaceGenerator(){
    var toHideSection = document.getElementById('ToHideLogin');
    toHideSection.remove();
    /* a laisser pour plus tard si on change d'avis - by William 
    Afin d'enlever le container favorites si l'user n'est pas connecte

    var divId = document.getElementById('FavoritesListInterface');
    var div = document.createElement('div');
    divId.appendChild(div);
    div.innerHTML = ` 

    <div class="card border-0 shadow my-5">
        <div class="card-body p-5">
            <div class="rounded-lg">
                <div><br>
                    <h1 class="font-weight-light">Votre liste de Favoris</h1>
                    <p style="font-style: oblique 40deg;">Connectez-vous pour avoir une liste</p><br>
                    <div id="Favorites">
                        <ul id="favoritesUl">
                        <ul>
                    </div>
                </div>
            </div>
        </div>
    </div>`;*/
}

/* Fonction de Deconnexion, efface les données persistent de l'utilisateur & le bouton Deconnexion et redirige vers l'Accueil */
function logout() {
	localStorage.removeItem('UserName');
	var toHide = document.getElementById('deconnexionGenerator');
    toHide.remove();
    alert("Déconnexion réussie !")
   	redirectAccueilPage();
}

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