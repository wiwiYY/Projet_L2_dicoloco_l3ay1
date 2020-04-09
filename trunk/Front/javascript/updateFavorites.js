/**
 * Ajout d'un favoris dans la liste des Favoris d'un user
 */
function addingFavoris() {    
    var favorisAdd = document.getElementById("wordName").textContent;
    if(localStorage.getItem('UserName') === null){
        alert("connectez vous");
	}
    else{
        console.log("je suis dans le else user exist");
        var username = localStorage.getItem('UserName');
        var requestURL = "http://localhost:8080/updateFavorites/"+favorisAdd+"/"+username+"/add";
        var request = new XMLHttpRequest();
        console.log(requestURL);
        request.open('GET', requestURL);
        request.responseType = 'json';
        request.send();
        alert("Le mot est ajouté dans votre liste des favoris.");
        document.location.href = "dicoloco_accueil.html";
    }
}

/**
 * Suppression d'un favoris dans la liste des Favoris d'un user
 */
function deleteFavoris() {
    var favorisDelete = document.getElementById("wordName").textContent;

    if(localStorage.getItem('UserName') === null){
        alert("connectez vous");
	}
    else{
        var username = localStorage.getItem('UserName');
        var requestURL = "http://localhost:8080/updateFavorites/"+favorisDelete+"/"+username+"/delete";
        var request = new XMLHttpRequest();
        request.open('GET', requestURL);
        request.responseType = 'json';
        request.send();

        alert("Le mot est supprimé de votre liste des favoris.");
        document.location.href = "dicoloco_accueil.html";
    }
}
