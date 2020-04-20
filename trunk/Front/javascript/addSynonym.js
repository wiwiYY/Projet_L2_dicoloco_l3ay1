function get_synList() {
    var synList = new Array;
    var synList_str = localStorage.getItem('syn');
    if (synList_str !== null) {
        synList = JSON.parse(synList_str); 
    }
    return synList;
}
 
function addSyn() {

    var task = document.getElementById('synonym').value;
    var synList = get_synList();

    if(task!==""){
        if(task.includes("_")){
            alert("Synonyme : contient '_'");
        }
        synList.push(task);
        localStorage.setItem('syn', JSON.stringify(synList));
        showSyn();
    }
    else{
        alert("c'est vide !");
    }

    return false;
}
 
function removeSyn() {
    var id = this.getAttribute('id');
    var synList = get_synList();
    synList.splice(id, 1);
    localStorage.setItem('syn', JSON.stringify(synList));
 
    showSyn();
 
    return false;
}
 
function showSyn() {
    var synList = get_synList();
 
    var html = '<ul>';
    for(var i=0; i<synList.length; i++) {
        html += '<li>' + synList[i] + '<button class="removeSyn" id="' +i  + '">x</button></li>';
    };
    html += '</ul>';
 
    document.getElementById('synList').innerHTML = html;
 
    var buttons = document.getElementsByClassName('removeSyn');
    for (var i=0; i < buttons.length; i++) {
        buttons[i].addEventListener('click', removeSyn);
    };
}

document.getElementById('addSyn').addEventListener('click', addSyn);
localStorage.removeItem('syn');

