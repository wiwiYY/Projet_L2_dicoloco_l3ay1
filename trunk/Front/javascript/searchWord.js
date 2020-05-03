/* Variable globale pour incrémenter le nb d'élément d'une liste pour aider à créé les balises html*/
let numSyn = 0;
//lien url vers l'API du Springboot
var urlAPI = "http://localhost:8080";
var nbLangue = 1;
/**
 * Recherche un mot
 */
function searchWord() {
    startLoading();
    redirectAncreLoading();

    var name = document.getElementById("name").value;
    var language = document.getElementById("languageDico").value;
    localStorage.setItem('Language', language);
    localStorage.setItem('WordName', name);

    if (name != "") {
        var requestURL = urlAPI + "/word/searchByLanguage/" +name+ "/" +language;
        var request = new XMLHttpRequest();

        // Supprimer le html pour le reload plus tard
        if(document.getElementById('toHideSection') !== null) {
            var toHideSection = document.getElementById('toHideSection');
            toHideSection.remove();
        }
        if(document.getElementById('toHideSuggestion') !==null) {
            var toHideSuggestion = document.getElementById('toHideSuggestion');
            toHideSuggestion.remove();
        }
        request.open('GET', requestURL);
        request.responseType = 'json';
        request.send();
        
        // Section réponse stocké
        request.onload = function () {
            var word = request.response;
            if (word == null) {
                searchSugg(name, language);
            } else {
                generateHtml(word);
            }
            
        }
    } else {
        alert("Veuillez entrer un mot pour faire une recherche");
    }
}

/**
 * Génère la liste des suggestions si le mot n'est pas trouvé
 * @param name 
 * @param language (mot anglais ou francais)
 */
function searchSugg(name, language) {
    var requestURL =  urlAPI + "/word/searchSuggestion/" + name+ "/" +language;
    var request = new XMLHttpRequest();
    request.open('GET', requestURL);
    request.responseType = 'json';
    request.send();

    request.onload = function () {
        var wordList = request.response;

        //Condition si la liste des suggestions n'est pas vide
        if (wordList != "") {
            generateSuggestionHtml(name);
            var numberOfSuggestion = 0;

            //generation des <li> pour les suggestions
            for(var i = 0; i<wordList.length; i++) {

                //appel a la method qui genere la liste
                var ula = document.getElementById('generateSuggestion');

                if (wordList[i] != null) {
                        var lia = document.createElement('li');

                        ula.appendChild(lia);
                        lia.innerHTML = `<button type="button" class="btn btn-outline-secondary btn-sm" id="suggestion`+numberOfSuggestion+`" onClick="searchWord4(`+numberOfSuggestion+`)">`+ wordList[i].name +`</button>`;
                        numberOfSuggestion++;
                }	
            }
        }else{
            alert("Aucune suggestion disponible. Verifiez la langue");
        }
        endLoading();
    }
}

/**
 * Recherche un mot depuis la liste des traductions d'un mot
 * @param {la n-ièeme <buttom>} num //
 */
function searchWord2(numSyn) {
    startLoading();
    redirectAncreLoading();
    var word = document.getElementById("synonymeNum" + numSyn).textContent; //ou utiliser innerHTML si y a un pb
    
    var languageChoose = localStorage.getItem('Language');

    // Remise à zéro de la variable pour une nouvelle recherche
    numSynZero(numSyn);

    // Supprimer le html pour le reload plus tard
    var toHideSection = document.getElementById('toHideSection');
    toHideSection.remove();

    var requestURL = urlAPI + "/word/searchByLanguage/" + word + "/" +languageChoose;
    var request = new XMLHttpRequest();
    request.open('GET', requestURL);
    request.responseType = 'json';
    request.send();

    // Section réponse stocké
    request.onload = function () {
        var word = request.response;
        generateHtml(word);
    }
}

/*Recherche un mot depuis la liste de traduction*/
function searchWord2b(language, word) {
    startLoading();
    redirectAncreLoading();
    // Supprimer le html pour le reload plus tard
    var toHideSection = document.getElementById('toHideSection');
    toHideSection.remove();

    var requestURL = urlAPI + "/word/searchByLanguage/" + word+ "/" +language;
    var request2 = new XMLHttpRequest();
    request2.open('GET', requestURL);
    request2.responseType = 'json';
    request2.send();

    // Section réponse stocké
    request2.onload = function () {
        var word = request2.response;
        generateHtml(word);
    }
}
var word = "";
/**
 * Recherche un mot depuis la liste de favoris d'un user 
 * @param mot à chercher
 */
function searchWord3(word) {
    startLoading();
    redirectAncreLoading();
    // Supprimer le html pour le reload plus tard
    var element = document.getElementById("favToSearch"+word).textContent;
    var elementTab = element.split(' | ');

    if(document.getElementById('toHideSection') !==null) {
        var toHideSection = document.getElementById('toHideSection');
        toHideSection.remove();
    }

    if(document.getElementById('toHideSuggestion') !==null) {
        var toHideSuggestion = document.getElementById('toHideSuggestion');
        toHideSuggestion.remove();
    }

    var requestURL = urlAPI + "/word/searchByLanguage/"+elementTab[0]+"/"+elementTab[1];
    var request2 = new XMLHttpRequest();
    request2.open('GET', requestURL);
    request2.responseType = 'json';
    request2.send();

    // Section réponse stocké
    request2.onload = function () {
        var word = request2.response;
        localStorage.setItem("Language", word['language']);
        generateHtml(word);
    }
}

/**
 * Recherche un mot depuis la liste des suggestions
 * @param {la n-ièeme <buttom>} num //
 */
function searchWord4(number) {
    startLoading();
    redirectAncreLoading();
    // Supprimer le html pour le reload plus tard
    var suggestionSelected = document.getElementById("suggestion"+number).textContent;
    var languageChoose = localStorage.getItem('Language');

    if(document.getElementById('toHideSection') !==null) {
        var toHideSection = document.getElementById('toHideSection');
        toHideSection.remove();
    }

    if(document.getElementById('toHideSuggestion') !==null) {
        var toHideSuggestion = document.getElementById('toHideSuggestion');
        toHideSuggestion.remove();
    }

    var requestURL = urlAPI + "/word/searchByLanguage/" +suggestionSelected+ "/" +languageChoose;
    var request2 = new XMLHttpRequest();
    request2.open('GET', requestURL);
    request2.responseType = 'json';
    request2.send();

    // Section réponse stocké
    request2.onload = function () {
        var word = request2.response;
        generateHtml(word);
    }
}
/**
 * Recherche un mot depuis la traduction
 * @param {le numero de la liste} word 
 */
function searchWord5(word) {
    startLoading();
    redirectAncreLoading();
    // Supprimer le html pour le reload plus tard
    var element = document.getElementById("tradToSearch"+word).textContent;
    var elementTab = element.split(' | ');

    if(document.getElementById('toHideSection') !==null) {
        var toHideSection = document.getElementById('toHideSection');
        toHideSection.remove();
    }

    if(document.getElementById('toHideSuggestion') !==null) {
        var toHideSuggestion = document.getElementById('toHideSuggestion');
        toHideSuggestion.remove();
    }

    var requestURL = urlAPI + "/word/searchByLanguage/"+elementTab[0]+"/"+elementTab[1];
    var request2 = new XMLHttpRequest();
    request2.open('GET', requestURL);
    request2.responseType = 'json';
    request2.send();

    // Section réponse stocké
    request2.onload = function () {
        var word = request2.response;
        localStorage.setItem("Language", word['language']);
        generateHtml(word);
    }
}

/**
 * Remet à zéro la valeur de la variable globale , nécessaire pour générer les liste à puces
 */
function numSynZero() {
    let numSyn = 0;
}

/**
 * Générer les listes à puces de synonyme
 */
function createSynonymList() {
    var ul = document.getElementById('wordSynonyms');
    var li = document.createElement('li');
    /*     li.setAttribute('id', 'synonymeNum'+numSyn); */
    ul.appendChild(li);
    li.innerHTML = ` <button class="btn btn-outline-secondary btn-sm" id="synonymeNum` + numSyn + `" class="className2" onClick="searchWord2(` + numSyn + `)" ></button>`;
    numSyn += 1;
}

/**
 * Générer les listes à puces de traduction
 */
function createTraductionList(numTrad, word) {
    var ul = document.getElementById('wordTraduction');
    var li = document.createElement('li');
    ul.appendChild(li);
    li.innerHTML = ` <button class="btn btn-outline-secondary btn-sm" id="tradToSearch` + numTrad + `" class="className3" onClick="searchWord5(` + numTrad + `)" >`+word['name']+` | `+word['language']+`</button>`;
}

/**
 * Générer un message lorsqu'il n'y a pas de traduction
 */
function createNoTraductionList() {
    var ul = document.getElementById('wordTraduction');
    var li = document.createElement('div');
    ul.appendChild(li);
    li.innerHTML = `<p>Aucune traduction disponible pour ce mot</p>`;
}

/**
 * Générer les liste de synonymes à puces de synonyme dans le Modal
 */
function createSynonymListModal(mot) {
    var ul = document.getElementById('wordSynonymsModal');
    var li = document.createElement('li');
    ul.appendChild(li);
    li.innerHTML = ` <button class="btn btn-outline-secondary btn-sm" id="synonymeNumModal` + numSyn + `"onClick="deleteSynonym(` + numSyn + `)" >`+mot+`</button>`;
    numSyn += 1;
}

/**
 * Génère code html de définition + Loading des informations du mot recherché
 * @param {word} jsonObj 
 */
function generateHtml(jsonObj ) {
    // Condition si le html de definition est généré
    if (document.querySelector('section') == null) {
        // Condition si exist affichage à cacher
        if (document.getElementById('toHideAccueil') !== null) {
            var toHide1 = document.getElementById('toHideAccueil');
            var toHide2 = document.getElementById('toHideLogo');
            toHide1.remove();
            toHide2.remove();
        }
        defNameGenerator();
    }

    // Loading des attributs json sur html
    document.querySelector("#wordName").innerHTML = jsonObj['name'];

    if(jsonObj['gender'] === "rien"){ //si il n'a pas de type alors il ne laffiche pas
        document.querySelector("#wordName2").innerHTML = jsonObj['category'] + ', ' + jsonObj['language'];
    }
    else{
        document.querySelector("#wordName2").innerHTML = jsonObj['gender'] + ', ' + jsonObj['category'] + ', ' + jsonObj['language'];
    }
    
    // Split des definitions de word pour les séparers dans une liste ou tableau
    var defs = jsonObj['definitions'];
    const defs1 = JSON.stringify(defs);
    var resDefs = defs1.split('_');
    var afficheDef = 0;
    var moreThanThreeDef = false;

    var toHideSection = document.getElementById('buttonForDef');
    toHideSection.remove();

    for(var j = 0; j<resDefs.length-1; j++){
        var definition = null;
        //supprimer les symbole inutle à l'affichage car cela créé un bug
        if (resDefs.length == 1) { //si y'a 1
            definition = resDefs[j].substring(2, resDefs[j].length - 2);
        }
        else if (j == 0) { //le premier si y'a plusieurs
            definition = resDefs[j].substring(2, resDefs[j].length);
        }
        else if (j == resDefs.length - 1) { //le dernier si y'a plusieurs
            definition = resDefs[j].substring(0, resDefs[j].length - 2);
        }
        else{ //les éléments intermédiaires si y'a plusieurs
            definition = resDefs[j].substring(3, resDefs[j].length);
        }

        if(afficheDef < 3) {
            var p = document.getElementById('wordDefinition');
            var writeDef = document.createElement('p');
            p.appendChild(writeDef);
            writeDef.innerHTML = definition + '<hr>';
            afficheDef++;
        } else {
            var p = document.getElementById('wordDefinition2');
            var writeDef = document.createElement('p');
            p.appendChild(writeDef);
            writeDef.innerHTML = definition +'<hr>';
            moreThanThreeDef = true;
        }
    }
    
    if (moreThanThreeDef === true) {
        var button = document.createElement('button');
        button.setAttribute("class", "btn btn-outline-secondary");
        button.setAttribute("id", "buttonForDef");
        button.setAttribute("data-toggle", "collapse");
        button.setAttribute("data-target", "#DefInfo");
        button.innerHTML = `+ de définitions`;
        document.getElementById('showDefButton').appendChild(button);
    }

    // Split les synonymes de word
    var str = jsonObj['synonyms'];
    const str1 = JSON.stringify(str);
    var res = str1.split(",");


    //les 2 boucle pour afficher le synonyme se fait en 2 temps et doivent être généré séparément car y'a un pb
    //condition si synonyme n'existe pas il va donner "[]"
    if(res[0] !== "[]"){
    
        //faire une boucle tant que exist un element
        for (var i = 0; i < res.length; i++) {
            //supprimer les symbole inutle à l'affichage car cela créé un bug
            if (res.length == 1) { //si y'a 1
                var mot = res[i].substring(2, res[i].length - 2);
            }
            else if (i == 0) { //le premier si y'a plusieurs
                var mot = res[i].substring(2, res[i].length - 1);
            }
            else if (i == res.length - 1) { //le dernier si y'a plusieurs
                var mot = res[i].substring(1, res[i].length - 2);
            }
            else { //les élément intermédiaire si y'a plusieurs
                var mot = res[i].substring(1, res[i].length - 1);
            }
            // ajout de l'élément
            createSynonymList();
            document.getElementById('synonymeNum' + i).textContent = mot; //inneHTML ou textContent
            
        }
    }
    //condition si synonyme n'existe pas il va donner "[]"
    if(res[0] !== "[]"){
    
        //faire une boucle tant que exist un element
        for (var i = 0; i < res.length; i++) {
            
            //supprimer les symbole inutle à l'affichage car cela créé un bug
            if (res.length == 1) {
                var mot = res[i].substring(2, res[i].length - 2);
            }
            else if (i == 0) {
                var mot = res[i].substring(2, res[i].length - 1);
            }
            else if (i == res.length - 1) {
                var mot = res[i].substring(1, res[i].length - 2);
            }
            else {
                var mot = res[i].substring(1, res[i].length - 1);
            }
            // ajout de l'élément
            createSynonymListModal(mot);
        }
    }

    //genere la liste des traduction 
    if (jsonObj['language']==="fr"){
        
        generateTraduction("en", nbLangue);
    }
    else{
        generateTraduction("fr", nbLangue);
    }

    numSyn = 0;
    endLoading();
}

/**
 * Génère le code html bootrstrap pour la page de définition
 */
function defNameGenerator() {
    const div = document.createElement('div');

    div.className = 'defContainerHtmlGenerator';

    div.innerHTML = `
    <!--généré par searchWord.js -->
    <section id="toHideSection">
        <div class="container">
            <div class="card border-0 shadow my-5">
                <div class="card-body p-5">
                    <h1 class="font-weight-light">Définition</h1><br>

                    <div id="toGenerateButton" class="row">
                        <div class="col-sm-4 col-md-4">
                            <h2 id="wordName"></h2>
                            <p id="wordName2"></p>
                        </div>
                    </div><br>

                    <div class="row">
                        <div class="col-sm-4 col-md-4">
                            <h3 class="font-weight-light">Définition(s)</h3>
                        </div>

                        <div class="col-sm-8 col-md-8">
                            <div>
                                <p id="wordDefinition"></p>
                            </div>
                            <div>
                                <a id="showDefButton">
                                    <button class="btn btn-outline-secondary" id="buttonForDef" data-toggle="collapse" data-target="#DefInfo">+ de définitions</button>
                                </a>
                                <div id="DefInfo" class="collapse">
                                    <div id="wordDefinition2"><br>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div><br>
                    
                    <!-- Modal pour modification de synonyme-->
                    <div class="row">
                        <div class="col-sm-4 col-md-4">
                            <h3 class="font-weight-light">Synonyme(s)</h3>
                            <!-- Button trigger modal -->
                            <button type="button" class="btn btn-outline-info" data-toggle="modal" data-target="#exampleModal">Modification</button>

                            <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h3 class="modal-title" id="exampleModalLabel">Modification</h5>
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                        <div class="modal-body">
                                            <h5 class="font-weight-light">Ajouter un synonyme</h3>
                                            <input type="text" id="synonymAdd" class="form-control" placeholder=""><br>

                                            <h5 class="font-weight-light">Supprimer un synonyme</h3>
                                            <input type="text" id="synonymDelete" class="form-control" placeholder=""><br>

                                            <!-- génère la liste de synonym dans le Modal -->
                                            <h5 class="font-weight-light">ou cliquez sur le synonyme que vous souhaitez supprimer :</h3>
                                            <ul id="wordSynonymsModal">
                                            </ul>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-outline-secondary" data-dismiss="modal">Annuler</button>
                                            <button type="button" onclick="addingSynonym()" class="btn btn-outline-success">Ajouter</button>
                                            <button type="button" onclick="deletingSynonym()" class="btn btn-outline-danger">Supprimer</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-8 col-md-8">
                            <!-- génère la liste de synonym -->
                            <ul id="wordSynonyms">
                            <ul>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-sm-4 col-md-4">
                            <h3 class="font-weight-light">Autre(s) langue(s)</h3>
                        </div>
                        <div class="col-sm-8 col-md-8">
                            <ul id="wordTraduction">
                                
                            </ul>
                        </div>
                    </div><br>

                </div>
            </div>
        </div>
    </section>`;
    document.getElementById('jsToGet').appendChild(div);

    /**
     * Cette partie du code concerne la génération du bouton 
     * ajouter ou supprimer un favoris de la liste
     */
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
        }
    };
    var username = localStorage.getItem('UserName');
    if (username != null) {
        
        //Envoi de la requete HTTP
        xhttp.open("GET", urlAPI + "/login/" +username, true);
        xhttp.responseType = 'json';
        xhttp.send();
     
        var generateButtonFavorite = document.getElementById('toGenerateButton');
        var exist = false;
        xhttp.onload = function () {
            var user = xhttp.response;
            var wordToCompare = document.getElementById("wordName").textContent+" | "+localStorage.getItem('Language');

            
            for(var i =0; i<taille(user.favorites); i++) {
                
                //si le mot est en favoris -> delete button
                if(wordToCompare===user.favorites[i]) {
                    exist = true;
                    i=user.favorites;
                }
            }
            if(exist){
                generateButtonFavorite.innerHTML += `<a id="deleteFavoris" onclick="deleteFavoris()">
                <!--généré par searchWord.js -->
                <img src="../img/fav_icon_3.png" width="40px" height="40px" alt="" onmouseover="this.src='../img/fav_icon_1.png';" onmouseout="this.src='../img/fav_icon_3.png';" 
                data-toggle="tooltip" data-placement="right" title='supprimer des favoris'/>
                </a>`;
            }
            else{
                generateButtonFavorite.innerHTML += `<a id="favorisAdd" onclick="addingFavoris()">
                <!--généré par searchWord.js -->
                <img src="../img/fav_icon_1.png" width="40px" height="40apx" alt="" onmouseover="this.src='../img/fav_icon_3.png';" onmouseout="this.src='../img/fav_icon_1.png';" 
                data-toggle="tooltip" data-placement="right" title='ajouter au favoris'/>
                </a>`;
            }
        }
    }
    redirectAncre();
}

/**
 * Genere l'affichage des suggestions
 * @param {*} name 
 */
function generateSuggestionHtml(name){
    var div = document.getElementById('toGenerateSuggestion');
    var diva = document.createElement('div');

    div.appendChild(diva);
    diva.innerHTML = 
    `<!--généré par searchWord.js -->
    <div id="toHideSuggestion" class="card border-0 shadow my-5">
        <div class="card-body p-5">
            <div class="rounded-lg">
                <div><br>
                    <h1 class="font-weight-light">Suggestion de mot</h1>
                    <h2 class="font-weight-light">Mot "`+name+`" non trouvé ! Essayez avec l'orthogaphe suivant :</h2><br>
                    <div id="suggestion">
                        <ul id="generateSuggestion">
                        <ul>
                    </div>
                </div>
            </div>
        </div>
    </div>`;  
    redirectAncre();  
}

/* Fonction qui genere la traduction du mot en question */
function generateTraduction(language, nbMot){
    var wordName = document.getElementById('wordName').textContent;
    var requestURL = urlAPI + "/word/translation/" + wordName +"/"+ language;
    var request = new XMLHttpRequest();
    request.open('GET', requestURL);
    request.responseType = 'json';
    request.send();

    // Section réponse stocké
    request.onload = function () {
        
        var word = request.response;
        if(word == null){
            createNoTraductionList();
        }else{
            for (var i=0; i<nbMot; i++){  
                createTraductionList(i , word);
            }
        }
    }
}

/* Fonction qui redirige sur l'endroit de la page ou il y a la definition  */
function redirectAncre() {
    window.scroll(0,700);
}

/* Fonction qui redirige sur l'endroit de la page ou il y a le laoding */
function redirectAncreLoading() {
    window.scrollTo(0,0);
}

/* Affiche/Enleve le loading */
function startLoading(){
    endLoading();
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

/* Fonction qui met fin au Loading */
function endLoading(){    
    if(document.getElementById('toEndLater') !== null) {
        var toHideSection = document.getElementById('toEndLater');
        toHideSection.remove();
    }
}