// TODO revoir les conditions d'inscription if else

/* Fonction main pour la connexion utilisateur */
function registerAction() { 
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			/*alert(this.response);*/
        }
    };

	/* Récupère le nom entrer par utilisateur */
    var username = document.getElementById("registerUser").value;

	if (username != "") {
		/*Envoi de la requete HTTP*/
		xhttp.open("GET", "http://localhost:8080/create/" +username, true);
		xhttp.responseType = 'json';
		xhttp.send();

		xhttp.onload = function () {
	        var user = xhttp.response;

	        if (user != null) {
	        	var inputUserName = user['name'];
	        	alert("Succès : Création réussie du compte utilisateur '"+inputUserName+"'. Veuillez à présent vous connecter dans la partie Connexion."); 
	        	document.location.href = "dicoloco_connexion.html";
	        } else {
	        	alert("Erreur : Compte utilisateur déjà existant ! Veuillez entrer un autre nom.");
	        }
	    }
	} else {
		alert("Inscription : Veuillez entrer un nom d'utilisateur");
	}
	
}