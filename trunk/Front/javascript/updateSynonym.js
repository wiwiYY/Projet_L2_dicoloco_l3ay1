//lien url vers l'API du Springboot
var urlAPI = "http://localhost:8080";

/**
 * Ajout d'un mot dans la liste des synonymes
 */
function addingSynonym () {
    var synonymAdd = document.getElementById("synonymAdd").value;

    if (synonymAdd != "") {
        if(synonymAdd.includes("_") || synonymAdd.includes("|")){
            alert("Un synonyme ne doit pas contenir '_' ou '|'");
        }
        else{
            var wordName = document.getElementById("wordName").textContent;
            var wordLanguage = localStorage.getItem('Language');

            var requestURL = urlAPI + "/word/update/"+wordName+"/"+synonymAdd+"/"+wordLanguage+"/add";
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
                    alert("Erreur : Le mot "+synonymAdd+" n'existe pas dans le dictionnaire '"+wordLanguage+"'");
                } else if (response == 4) {
                    alert("Erreur : Le synonyme fait déjà partie de la liste de synonyme de "+wordName);
                } else if (response == 5) {
                    alert("Erreur : La methode n'existe pas");
                } else {
                    alert("Erreur message de réponse par défaut : Contacter nous afin de régler ce problème");
                }
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
    var wordLanguage = localStorage.getItem('Language');

    if (synonymDelete != "") {
        var wordName = document.getElementById("wordName").textContent;
        var requestURL = urlAPI + "/word/update/"+wordName+"/"+synonymDelete+"/"+wordLanguage+"/delete";
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
    var syn = document.getElementById("synonymeNumModal"+numSyn).textContent;

    var wordName = document.getElementById("wordName").textContent;
    var wordLanguage = localStorage.getItem('Language');
    var requestURL = urlAPI + "/word/update/"+wordName+"/"+syn+"/"+wordLanguage+"/delete";
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
