var urlAPI = "http://localhost:8080";

//variable pour générer les questions
var questionD = null;
var answerD = null;
var correctAnswerD = null;
var q1 = null;
var q2 = null;
var q3 = null;
var jsonQ = null;

var buildAgain = 0;
/**
 * generate random position for the correct answer
 */
function generePosition(){
    var res = "";
    var alphabet = "abcd";
    res += alphabet[Math.floor(Math.random() * alphabet.length)];
    // console.log(res);
    return res;
}
function reponse(word, language, number) {
    // console.log("je recois en reponse: "+word+", " +language+", "+number+".");
    var requestURL = urlAPI + "/word/searchByLanguage/"+word +"/"+ language;
    var request = new XMLHttpRequest();
    request.open('GET', requestURL);
    request.responseType = 'json';
    request.send();

    request.onload = function () {
        var mot = request.response;

        questionD = `Quel est le bon synonyme du mot "` + mot['name'] + `" ?`;

        var str = mot['synonyms'];
        const str1 = JSON.stringify(str);
        var res = str1.split(",");
        console.log("Les synonymes de "+ mot['name'] +" sont : "+res);
        
        if (res.length == 1) { //si y'a 1
            answersD = res[0].substring(2, res[0].length - 2);
        }else{
            answersD = res[0].substring(2, res[0].length - 1);
        }
        
        reponseGenerator(questionD, mot['name'], answersD, number);

        // console.log("question généré");
    }
}

/**
 * genere la reponse 
 */
function reponseGenerator(questionD, noWord, noSynonym, number){

    // console.log('je recois ########### '+noWord+', '+noSynonym+', '+number);
    var reqURL = urlAPI + "/word/getRandomAnswer/"+noWord+"/"+noSynonym;
    var req = new XMLHttpRequest();
    req.open('GET', reqURL);
    req.responseType = 'json';
    req.send();
    req.onload = function () {
        var word = req.response;
        // console.log("resultat ", word);
        correctAnswerD = generePosition();

        //genere la question au format json
        jsonQ = {
            question: questionD,
            answers: {
                a: "",
                b: "",
                c: "",
                d: "",
            },
            correctAnswer: correctAnswerD
        }

        var positionFalseAnswer = 0
        for (letter in jsonQ.answers){

            if(letter === correctAnswerD){
                jsonQ.answers[letter] = capitalizeFirstLetter(noSynonym);
                // console.log("le bon resultat de "+noWord+" est "+noSynonym);
            }
            else {
                jsonQ.answers[letter] = word[positionFalseAnswer]['name'];
            }
            positionFalseAnswer++;
        }
        //asignement de la question à quel chiffre
        // console.log("le mot "+noWord+", le n°"+number);
        switch(number){
            case 0: q1 = jsonQ; break;
            case 1: q2 = jsonQ; break;
            case 2: q3 = jsonQ; break;
        }
        createSubmit();
        generateQuizz();
        generateQuizz();
        endLoading();
    }
}
function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}
/**
 * genere la liste des questions
 */
function startQuizz() {
    const resultsContainer = document.getElementById('results');
    
    startLoading();
    var requestURL = urlAPI + "/word/getRandom";
    var request = new XMLHttpRequest();
    request.open('GET', requestURL);
    request.responseType = 'json';
    request.send();

    request.onload = function () {
        var wordList = request.response;

        //Condition si la liste des suggestions n'est pas vide
        if (wordList != "") {

            for (var i = 0; i < wordList.length; i++) {

                if (wordList[i] != null) {
                    // console.log("le mot n°" + i + " : " + wordList[i]['name'] + " et la langue :"+wordList[i]['language']);
                    reponse(wordList[i]['name'], wordList[i]['language'], i);
                }
            }
        } else {
            alert("Aucun mot disponible.");
        }
        resultsContainer.innerHTML = "";
    }
}


//http://info.clg.qc.ca/java/elements/generer-nombres-aleatoires
/**
 * genere la liste des questions au format json
 */
function generateQuizz() {
    const myQuestions = [
        q1,
        q2,
        q3
    ];
    
    function buildQuiz() {
        // variable to store the HTML output
        const output = [];

        // for each question...
        myQuestions.forEach(
            (currentQuestion, questionNumber) => {
                // the code we want to run for each question goes here
                // variable to store the list of possible answers
                const answers = [];

                // and for each available answer...
                for (letter in currentQuestion.answers) {

                    // ...add an HTML radio button
                    // console.log('ici : ' + questionNumber);
                    answers.push(
                        `<label>
                        <input type="radio" name="question${questionNumber}" value="${letter}">&nbsp;&nbsp;&nbsp;&nbsp;
                        ${letter})
                        ${currentQuestion.answers[letter]}
                        </label><br>`
                    );
                }

                // add this question and its answers to the output
                output.push(
                    `<br><hr><br><div class="question"> ${currentQuestion.question} </div><p></p>
                <div class="answers"> ${answers.join('')} </div>`
                );
            }
        );

        // finally combine our output list into one string of HTML and put it on the page
        quizContainer.innerHTML = output.join('');
    }


    function showResults() {
        // gather answer containers from our quiz
        const answerContainers = quizContainer.querySelectorAll('.answers');

        // keep track of user's answers
        let numCorrect = 0;

        // for each question...
        myQuestions.forEach((currentQuestion, questionNumber) => {

            // find selected answer
            const answerContainer = answerContainers[questionNumber];
            const selector = `input[name=question${questionNumber}]:checked`;
            const userAnswer = (answerContainer.querySelector(selector) || {}).value;

            // if answer is correct
            if (userAnswer === currentQuestion.correctAnswer) {
                // add to the number of correct answers
                numCorrect++;

                // color the answers green
                answerContainers[questionNumber].style.color = 'lightgreen';
            }
            // if answer is wrong or blank
            else {
                // color the answers red
                answerContainers[questionNumber].style.color = 'red';
            }
        });

        // show number of correct answers out of total
        var pluriel = "réponse correcte";
        if ((numCorrect) >= 2) {
            pluriel = "réponses correctes";
        }
        // alert(`${numCorrect} sur ${myQuestions.length} ` + pluriel)
        resultsContainer.innerHTML = `${numCorrect} sur ${myQuestions.length} ` + pluriel;
    }

    const quizContainer = document.getElementById('quiz');
    const resultsContainer = document.getElementById('results');
    const submitButton = document.getElementById('submit');

    // display quiz right away
    buildQuiz();

    // on submit, show results
    submitButton.addEventListener('click', showResults);
}

/**
 * Affiche/Enleve le loading 
 */
function startLoading(){
    endLoading();
    console.log('loading start');
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

function endLoading(){    
    if(document.getElementById('toEndLater') !== null) {
        var toHideSection = document.getElementById('toEndLater');
        toHideSection.remove();
        console.log('loading end');
    }
}
  function createSubmit(){
    var div = document.getElementById('subButton');
    div.innerHTML = 
    `<br><button id="submit" class="btn btn-primary"  style="width: 100%;" >Valider</button>`;
  }