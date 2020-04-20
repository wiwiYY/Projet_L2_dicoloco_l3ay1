/* Lien url vers l'API du Springboot */
var urlAPI = "http://localhost:8080";

/* Ajout d'un favoris dans la liste des Favoris d'un user */
function addingFavoris() {    
    var favorisAdd = document.getElementById("wordName").textContent;
    var wordInfo = document.getElementById("wordName2").textContent;
    
    var wordInfoTab = wordInfo.split(', ');

    /* La langue du mot a mettre en favoris */
    var language = wordInfoTab[2];

    localStorage.setItem('Language', wordInfoTab[2]);

    if(localStorage.getItem('UserName') === null){
        alert("connectez vous");
	}
    else{
        var username = localStorage.getItem('UserName');
        var requestURL = urlAPI + "/updateFavorites/"+favorisAdd+"/"+language+"/"+username+"/add";
        var request = new XMLHttpRequest();

        request.open('GET', requestURL);
        request.responseType = 'json';
        request.send();

        alert("Le mot est ajouté dans votre liste des favoris.");
        document.location.href = "dicoloco_accueil.html";
    }
}

/* Suppression d'un mot de la liste des Favoris de l'user */
function deleteFavoris() {
    var favorisDelete = document.getElementById("wordName").textContent;

    if(localStorage.getItem('UserName') === null){
        alert("Erreur : Contactez le support et signaler 'Erreur suppression de Favoris'");
	}
    else{
        var username = localStorage.getItem('UserName');
        var requestURL = urlAPI + "/updateFavorites/"+favorisDelete+"/"+localStorage.getItem('Language')+"/"+username+"/delete";
    
        var request = new XMLHttpRequest();
        request.open('GET', requestURL);
        request.responseType = 'json';
        request.send();

        alert("Le mot est supprimé de votre liste des favoris.");
        document.location.href = "dicoloco_accueil.html";
    }
}
