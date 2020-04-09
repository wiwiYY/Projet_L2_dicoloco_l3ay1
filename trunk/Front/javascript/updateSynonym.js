/**
 * Ajout d'un mot dans la liste des synonymes
 */
function addingSynonym () {
    var synonymAdd = document.getElementById("synonymAdd").value;

    if (synonymAdd != "") {
        var wordName = document.getElementById("wordName").textContent;
        var requestURL = "http://localhost:8080/word/update/"+wordName+"/"+synonymAdd+"/add";
        var request = new XMLHttpRequest();

        request.open('GET', requestURL);
        request.responseType = 'json';
        request.send();
        
        request.onload = function () {
            var response = request.response;
            
            if (response == 1) {
                alert("Succès : Mise à jour réussie");
                document.location.href = "dicoloco_accueil.html";
            } else if (response == 2) {
                alert("Erreur : Le mot "+wordName+" n'existe pas dans le dictionnaire");
            } else if (response == 4) {
                alert("Erreur : Le synonyme fait déjà partie de la liste de synonyme de "+wordName);
            } else if (response == 5) {
                alert("Erreur : Le synonyme n'existe pas");
            } else {
                alert("Erreur message de réponse par défaut : Contacter nous afin de régler ce problème");
            }
        }
    } else {
        alert("Ajout de synonyme : Veuillez entrer le synonyme avant de l'ajouter");
    } 
}

/**
 * Suppression d'un mot de la liste des synonymes
 * Depuis le input
 */
function deletingSynonym () { 
    var synonymDelete = document.getElementById("synonymDelete").value;

    if (synonymDelete != "") {
        var wordName = document.getElementById("wordName").textContent;
        var requestURL = "http://localhost:8080/word/update/"+wordName+"/"+synonymDelete+"/delete";
        var request = new XMLHttpRequest();

        request.open('GET', requestURL);
        request.responseType = 'json';
        request.send();
        
        request.onload = function () {
            var response = request.response;

            if (response == 1) {
                alert("Succès : Mise à jour réussie");
                document.location.href = "dicoloco_accueil.html";
            } else if (response == 3) {
                alert("Erreur : Le synonyme "+synonymDelete+" n'existe pas dans la liste de synonyme du mot");
            } else {
                alert("Erreur message de réponse par défaut : Contacter nous afin de régler ce problème");
            }
        }
    } else {
        alert("Suppression de synonyme : Veuillez entrer le synonyme avant de le supprimer");
    }   
}

/**
 * Suppression d'un mot de la liste des synonymes
 * Depuis la liste de modal
 */
function deleteSynonym (numSyn) { 
    var mot = document.getElementById("synonymeNumModal"+numSyn).textContent;

    var wordName = document.getElementById("wordName").textContent;
    var requestURL = "http://localhost:8080/word/update/"+wordName+"/"+mot+"/delete";
    var request = new XMLHttpRequest();

    request.open('GET', requestURL);
    request.responseType = 'json';
    request.send();
    
    request.onload = function () {
        var response = request.response;

        if (response == 1) {
            alert("Succès : Mise à jour réussie");
            document.location.href = "dicoloco_accueil.html";
        } else if (response == 3) {
            alert("Erreur : Le synonyme "+synonymDelete+" n'existe pas dans la liste de synonyme du mot");
        } else {
            alert("Erreur message de réponse par défaut : Contacter nous afin de régler ce problème");
        }
    }
}
