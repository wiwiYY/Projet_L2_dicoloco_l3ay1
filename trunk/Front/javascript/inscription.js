/* Lien url vers l'API du Springboot */
var urlAPI = "http://localhost:8080";

/* Fonction main pour la connexion utilisateur */
function registerAction() { 
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			/*alert(this.response);*/
        }
    };

	/* Recupere le nom entrer par utilisateur */
    var username = document.getElementById("registerUser").value;

	if (username != "") {
		startLoading();
		/* Envoi de la requete HTTP */
		xhttp.open("GET", urlAPI + "/create/" +username, true);
		xhttp.responseType = 'json';
		xhttp.send();

		xhttp.onload = function () {
	        var response = xhttp.response;

	        if (response === 1) {
	        	alert("Succès : Création réussie du compte utilisateur '"+username+"'. Veuillez à présent vous connecter dans la partie Connexion."); 
	        	document.location.href = "dicoloco_connexion.html";
	        } else {
	        	alert("Erreur : Compte utilisateur déjà existant ! Veuillez entrer un autre nom.");
	        }
	    }
	    endLoading();
	} else {
		alert("Inscription : Veuillez entrer un nom d'utilisateur");
	}
}