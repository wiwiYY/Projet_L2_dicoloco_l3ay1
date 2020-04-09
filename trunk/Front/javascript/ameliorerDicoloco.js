/* Fonction pour créer un nouveau mot dans le dictionnaire */
function createWord() { 
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      // alert(this.responseText); 
    }
  };

  var nameAdd = document.getElementById("nameAdd").value;
  var genderAdd = document.getElementById("genderAdd").value;
  var categoryAdd = document.getElementById("categoryAdd").value;
  var languageAdd = document.getElementById("languageAdd").value;
  var listDef = document.getElementById("defList");
  var listSynonym = document.getElementById("synList");
  var definitionsAdd = "";
  var synonymsAdd = "";

  if (nameAdd == "") {
    alert("Veuillez entrer le nom du mot à créer !");
  } else { 
  
    var itemsDef = listDef.getElementsByTagName("li");
    var itemsSynonym = listSynonym.getElementsByTagName("li");

    var definitionsAdd = "";
    for (var i = 0; i < itemsDef.length; ++i) {
    var def =  (itemsDef[i].textContent || itemsDef[i].innerText);
      definitionsAdd += def.substring(0, def.length-1);
      definitionsAdd += "_";
    }

    var synonymsAdd = "";
    for (var i = 0; i < itemsSynonym.length; ++i) {
    var synonym =  (itemsSynonym[i].textContent || itemsSynonym[i].innerText);
      synonymsAdd += synonym.substring(0, synonym.length-1);
      synonymsAdd += "_";
    }

    if(definitionsAdd == "" && synonymsAdd != ""){
      xhttp.open("GET", "http://localhost:8080/word/create/"+nameAdd+"/"+"_"+"/"+genderAdd+"/"+categoryAdd+"/"+synonymsAdd+"/"+languageAdd, true);
      xhttp.setRequestHeader("Content-type", "application/json");
      xhttp.send();
    } else if (definitionsAdd != "" && synonymsAdd == ""){
      xhttp.open("GET", "http://localhost:8080/word/create/"+nameAdd+"/"+definitionsAdd+"/"+genderAdd+"/"+categoryAdd+"/_/"+languageAdd, true);
      xhttp.setRequestHeader("Content-type", "application/json");
      xhttp.send();
    } else if (definitionsAdd == "" && synonymsAdd == ""){
      xhttp.open("GET", "http://localhost:8080/word/create/"+nameAdd+"/"+"_"+"/"+genderAdd+"/"+categoryAdd+"/"+"_"+"/"+languageAdd, true);
      xhttp.setRequestHeader("Content-type", "application/json");
      xhttp.send();
    } else {
      xhttp.open("GET", "http://localhost:8080/word/create/"+nameAdd+"/"+definitionsAdd+"/"+genderAdd+"/"+categoryAdd+"/"+synonymsAdd+"/"+languageAdd, true);
      xhttp.setRequestHeader("Content-type", "application/json");
      xhttp.send();
    }

    /* Récupère la reponse, un objet User sous format JSON et génère les messages de reponses */
    xhttp.onload = function () {
      var response = xhttp.response;

      if (response == 0) {
          alert("Succès : Le mot '"+nameAdd+"' a bien été ajouté au dictionnaire.");
          document.location.href = "dicoloco_ameliorer.html";
      } else if (response == 1) {
          alert("Erreur : Le mot '"+nameAdd+"' n'a pas été ajouté au dictionnaire!");
          document.location.href = "dicoloco_ameliorer.html";
      } else if (response == 2) {
          alert("Erreur : Le mot '"+nameAdd+"' existe déjà dans ce dictionnaire !");
          document.location.href = "dicoloco_ameliorer.html";
      } else {
          alert("Erreur : La fonctionnalité Ajouter rencontre actuellement un problème technique, Veuillez nous contacter !");
          document.location.href = "dicoloco_ameliorer.html";
      }
    }
  }
}

/* Fonction pour supprimer un mot du dictionnaire */ 
function deleteWord() { 
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      // alert(this.responseText);
    }
  };

  var nameDelete = document.getElementById("nameDelete").value;

  if (nameDelete != "") {
    xhttp.open("GET", "http://localhost:8080/word/delete/"+nameDelete, true);
    xhttp.setRequestHeader("Content-type", "application/json");
    xhttp.send();
    
    /* Récupère la reponse, un objet User sous format JSON et génère les messages de reponses */
    xhttp.onload = function () {
      var response = xhttp.response;

      if (response == 0) {
          alert("Succès : Le mot '"+nameDelete+"' a bien été supprimé du dictionnaire.");
          deleteFavoris();
          document.location.href = "dicoloco_ameliorer.html";
      } else if (response == 1) {
          alert("Erreur : Le mot '"+nameDelete+"' n'a pas été supprimé du dictionnaire!");
          document.location.href = "dicoloco_ameliorer.html";
      } else if (response == 2) {
          alert("Erreur : Le mot '"+nameDelete+"' n'existe pas dans ce dictionnaire !");
          document.location.href = "dicoloco_ameliorer.html";
      } else {
          alert("Erreur : La fonctionnalité Supprimer rencontre actuellement un problème technique, Veuillez nous contacter !");
          document.location.href = "dicoloco_ameliorer.html";
      }
    }
  } else {
    alert("Suppression de mot : Veuillez entrer un mot")
  }
  
}

/* Si un compte utilisateur est connecte lors de la suppression d'un mot 
 * On update directement sa liste de favoris 
 * en supprimant de sa liste de favoris le mot supprime
 */
function deleteFavoris() {
  var favorisDelete = document.getElementById("nameDelete").value;

  if(localStorage.getItem('UserName') === null){
      alert("Connectez vous d'abord !");
  }
  else{
    var username = localStorage.getItem('UserName');

    var requestURL = "http://localhost:8080/updateFavorites/"+favorisDelete+"/"+username+"/delete";
    var request = new XMLHttpRequest();
    request.open('GET', requestURL);
    request.responseType = 'json';
    request.send();

    request.onload = function() {
      var response = request.response;
      alert(response);
      if (response == 2) {
        alert("Erreur : L'utilisateur n'existe pas !");
      } else if (response == 3) {
        alert("Erreur : Le mot ne fait pas partie de la liste de favoris de l'utilisateur !");
      } else if (response == 1) {
        alert("Le mot a été supprimé de votre liste des favoris.");
      } else {
        alert("Erreur : Veuillez nous contacter.");
      }
    }
  }
}

/* Fonction pour ajouter une liste de mot à partir de fichier JSON 
 * TODO mettre plus d'explication PAN 
 */
function addList(){
  startLoading()
  var inputFile = document.getElementById('inputFile').files[0];

  if (inputFile != null) {
    var reader = new FileReader();

    reader.onload = function(event) {
      alert(reader.result); 
      var xhr = new XMLHttpRequest();

      xhr.open("POST", "http://localhost:8080/word/listWords", true);
      xhr.setRequestHeader("content-type", "application/json");

      xhr.onreadystatechange = function (aEvt) {
        if (xhr.readyState == 4) {
          if (xhr.status == 200){
            console.log("request 200-OK");
          }
        }        
      };  
      xhr.send((reader.result));
    };
    reader.readAsText(inputFile);
  } else {
    alert("Liste de mot à ajouter : Veuillez choisir un fichier");
  }
  endLoading();
}

/* Fonction pour ajouter une liste de mot à partir de fichier JSON 
 * A partir de la BDD trouvée sur Internet
 */
function addList2 () {
  var inputFile = document.getElementById('inputFile2').files[0];

  if (inputFile != null) {
    var reader = new FileReader();
      
    reader.onload = function(event) {
      alert(reader.result);
      alert("Ajout de la bdd en cours"); 

      var xhr = new XMLHttpRequest();
      xhr.open("POST", "http://localhost:8080/word/listWords2", true);
      xhr.setRequestHeader("content-type", "application/json");

      xhr.onreadystatechange = function (aEvt) {

        if (xhr.readyState == 4) {

          if (xhr.status == 200){
            console.log("request 200-OK");
            alert("Ajout de la bdd terminé"); 
          }
        }        
      };    
      xhr.send((reader.result));
    };
    reader.readAsText(inputFile);
  } else {
    alert("Liste de mot à ajouter (Modèle en ligne) : Veuillez choisir un fichier");
  }
}

function startLoading(){
  console.log('loading start');
  var div = document.getElementById('loading_body');
  var diva = document.createElement('div');
  diva.id = 'toEndLater';
  div.appendChild(diva);
  diva.innerHTML = 
  `<div class="bar">
      <div class="circle"></div>
      <p class="loading">Loading</p>
  </div>`;
}

function endLoading(){
  var div = document.getElementById('toEndLater');
  div.parentNode.removeChild(div);
  console.log('loading end');
}
