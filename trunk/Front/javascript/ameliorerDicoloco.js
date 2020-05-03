//lien url vers l'API du Springboot
var urlAPI = "http://localhost:8080";

/* Fonction pour créer un nouveau mot dans le dictionnaire */
function createWord() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            // alert(this.responseText); 
        }
    };

    var nameAdd = document.getElementById("nameAdd").value;
    var genderAdd = document.getElementById("genderAdd").value;
    var categoryAdd = document.getElementById("categoryAdd").value;
    var languageAdd = document.getElementById("languageAdd").value;
    var listDef = document.getElementById("defList");
    var listSynonym = document.getElementById("synList");
    var definitionsAdd = "";
    var synonymsAdd = "";

    if (nameAdd == "") {
        alert("Veuillez entrer le nom du mot à créer !");
    }
    else if(nameAdd.includes("_")||nameAdd.includes("|")){
        alert("Le nom du mot ne peut contenir '_' ou '|'");
    }
    else {
        startLoading();
        var itemsDef = listDef.getElementsByTagName("li");
        var itemsSynonym = listSynonym.getElementsByTagName("li");
        var result = [];
        var definitionsAdd = [];
        var synonymsAdd = [];
        var obj = {};

        for (var i = 0; i < itemsDef.length; ++i) {
            var def = (itemsDef[i].textContent || itemsDef[i].innerText);
            definitionsAdd.push(def.substring(0, def.length - 1));
        }

        for (var i = 0; i < itemsSynonym.length; ++i) {
            var synonym = (itemsSynonym[i].textContent || itemsSynonym[i].innerText);
            synonymsAdd.push(synonym.substring(0, synonym.length - 1));
        }

        obj["name"] = nameAdd;
        obj["definitions"] = definitionsAdd;
        obj["synonyms"] = synonymsAdd;
        obj["gender"] = genderAdd;
        obj["language"] = languageAdd;
        obj["category"] = categoryAdd;

        result.push(obj);
        
        xhttp.open("POST", urlAPI + "/word/listWords", true);
        xhttp.setRequestHeader("content-type", "application/json;charset=UTF-8");
        xhttp.send(JSON.stringify(result));
       

        /* Récupère la reponse, un objet User sous format JSON et génère les messages de reponses */
        xhttp.onload = function () { 
            endLoading();
            var response = xhttp.response;

            if (response == 0) {
                alert("Succès : Le mot '" + nameAdd + "' ("+languageAdd+") a bien été ajouté au dictionnaire.");
                document.location.href = "dicoloco_ameliorer.html";
            } else if (response == 1) {
                alert("Erreur : Le mot '" + nameAdd + "' n'a pas été ajouté au dictionnaire! Veuillez vérifier la langue ou la syntaxe.");
            } else if (response == 2) {
                alert("Erreur : Le mot '" + nameAdd + "' ("+languageAdd+") existe déjà dans ce dictionnaire !");
            } else {
                alert("Erreur : La fonctionnalité Ajouter rencontre actuellement un problème technique, Veuillez nous contacter !");
                document.location.href = "dicoloco_ameliorer.html";
            }
        }
    }
}

/* Fonction pour supprimer un mot du dictionnaire */
function deleteWord() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            // alert(this.responseText);
        }
    };

    var nameDelete = document.getElementById("nameDelete").value;
    var languageDelete = document.getElementById("languageDelete").value;

    if (nameDelete != "") {
        startLoading();
        xhttp.open("GET", urlAPI + "/word/delete/" + nameDelete + "/" + languageDelete, true);
        xhttp.setRequestHeader("Content-type", "application/json");
        xhttp.send();

        /* Récupère la reponse, un objet User sous format JSON et génère les messages de reponses */
        xhttp.onload = function () {
            var response = xhttp.response;

            if (response == 0) {
                alert("Succès : Le mot '" + nameDelete + "' a bien été supprimé du dictionnaire.");
                document.location.href = "dicoloco_ameliorer.html";
            } else if (response == 1) {
                alert("Erreur : Le mot '" + nameDelete + "' n'a pas été supprimé du dictionnaire!");
            } else if (response == 2) {
                alert("Erreur : Le mot '" + nameDelete + "' n'existe pas dans ce dictionnaire !");
            } else {
                alert("Erreur : La fonctionnalité Supprimer rencontre actuellement un problème technique, Veuillez nous contacter !");
            }
        }
    } else {
        alert("Suppression de mot : Veuillez entrer un mot")
    }
    endLoading();
}

/* Si un compte utilisateur est connecte lors de la suppression d'un mot 
 * On update directement sa liste de favoris 
 * en supprimant de sa liste de favoris le mot supprime
 */
function deleteFavoris() {
    var favorisDelete = document.getElementById("nameDelete").value;

    if (localStorage.getItem('UserName') === null) {
        alert("Connectez vous d'abord !");
    }
    else {
        var username = localStorage.getItem('UserName');

        var requestURL = urlAPI + "/updateFavorites/" + favorisDelete + "/" + username + "/delete";
        var request = new XMLHttpRequest();
        request.open('GET', requestURL);
        request.responseType = 'json';
        request.send();

        request.onload = function () {
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
function addList() {
    startLoading();
    var inputFile = document.getElementById('inputFile').files[0];
    var type = document.getElementById('dataType').value;

    if (inputFile != null) {
        var reader = new FileReader();

        reader.onload = function (event) {
            var resultRest;
            if (type == 'csv') {
                resultRest = csvJSON(reader.result);
            }

            else {
                resultRest = reader.result;
            }
            var xhr = new XMLHttpRequest();

            xhr.open("POST", urlAPI + "/word/listWords", true);
            xhr.setRequestHeader("content-type", "application/json");

            xhr.onreadystatechange = function (aEvt) {
                if (xhr.readyState == 4) {
                    if (xhr.status == 200) {
                        alert("Succès : Le fichier a bien été ajouté.");
                        document.location.href = "dicoloco_ameliorer.html";       
                    } 
                    else {
                        alert("Erreur : Le fichier n’est pas reconnu !"); 
                    }
                }
                endLoading();
            };
            xhr.send((resultRest));
            
        };
        reader.readAsText(inputFile);
    } else {
        alert("Liste de mot à ajouter : Veuillez choisir un fichier");
    }
}

//Source : http://techslides.com/convert-csv-to-json-in-javascript
function csvJSON(csv){

    var lines=csv.split("\n");
  
    var result = [];
  
    var headers=lines[0].split(",");
  
    for(var i=1;i<lines.length;i++){
        var obj = {};
        var currentline=lines[i].split(",");
        var def = [];
        var syn = [];
        var startDef = false;
        var startSyn = false;
        var containsSyn = false;
        var containsDef = false;
  
        for(var j=0;j<headers.length;j++){
          if(headers[j].includes("definitions")){
            startDef = true;
            
            if(!currentline[j] == ("") || !currentline[j] == (",")){
              def.push(currentline[j]);
            }
          }
          else if(headers[j].includes("synonyms")){
            startSyn = true;
            
            if(!currentline[j] == ("") || !currentline[j] == (",")){
              syn.push(currentline[j]);
              containsSyn = true;
            }
          }
          else{
            if(startDef==true){
              obj["definitions"] = def;
              startDef = false;
              containsDef = true;
            }
            if(startSyn==true){
              obj["synonyms"] = syn;
              startSyn = false;
              containsSyn = true;
            }
            
            if(headers[j].includes("language")){
              var header = headers[j].replace("\r", "");
              var language = currentline[j].replace("\r", "");
              obj[header] = language;
            }
            else
              obj[headers[j]] = currentline[j];
          }
        }
        
        if(containsSyn == false){
          obj["synonyms"] = [];
        }
  
        if(containsDef == false){
          obj["definitions"] = [];
        }
        
  
        result.push(obj);
  
      }
    
    //return result; //JavaScript object
    return JSON.stringify(result); //JSON
    //return result; //JavaScript object
}

/**
 * Affiche/Enleve le loading 
 */
function startLoading() {
    if(document.getElementById('toEndLater') !== null) {
        var toHideSection = document.getElementById('toEndLater');
        toHideSection.remove();
    }
    var div = document.getElementById('loading_body');
    var diva = document.createElement('div');
    diva.id = 'toEndLater';
    div.appendChild(diva);
    diva.innerHTML =
        `<div class="bar">
      <div class="circle"></div>
      <p class="loading">Patientez</p>
  </div>`;
}

function endLoading() {
    var div = document.getElementById('toEndLater');
    div.parentNode.removeChild(div);
}

/* Fonction pour ajouter une liste de mots à partir de fichier JSON
 *  pour la bdd FR
 */
function addListBdd() {
    var inputFile = document.getElementById('inputFileFR').files[0];

    var reader = new FileReader();

        reader.onload = function (event) { 
            var resultRest = reader.result;
            var json = JSON.parse(resultRest);
            var result = [];
           
            for(var i =0; i<json.length;i++){
                var obj = {};
                var wordJSON = json[i];
                obj["name"] = wordJSON.M.mot;
                obj["definition"] = wordJSON.SENS;
                obj["category"] = wordJSON.CA.categorie + " " + wordJSON.CA.genre;
                result.push(obj);
            }
            alert(JSON.stringify(result));

            var xhr = new XMLHttpRequest();
            xhr.open("POST", urlAPI + "/word/listWordsFr", true);
            xhr.setRequestHeader("content-type", "application/json;charset=UTF-8");
           
        xhr.send(JSON.stringify(result));   
        };
        
    reader.readAsText(inputFile);
    
}
