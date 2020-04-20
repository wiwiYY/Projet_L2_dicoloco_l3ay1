function get_defList() {
    var defList = new Array;
    var defList_str = localStorage.getItem('def');
    if (defList_str !== null) {
        defList = JSON.parse(defList_str); 
    }
    return defList;
}
 
function addDef() {

    var task = document.getElementById('definition').value;
    var defList = get_defList();

    if(task!==""){
        if(task.includes("_")){
            alert("Definition : contient '_'");
        }
        else{
            defList.push(task);
            localStorage.setItem('def', JSON.stringify(defList));
            showDef();
        }
    }
    
    else{
        alert("c'est vide !");
    }

    return false;
}
 
function removeDef() {
    var id = this.getAttribute('id');
    var defList = get_defList();
    defList.splice(id, 1);
    localStorage.setItem('def', JSON.stringify(defList));
 
    showDef();
 
    return false;
}
 
function showDef() {
    var defList = get_defList();
 
    var html = '<ul>';
    for(var i=0; i<defList.length; i++) {
        html += '<li>' + defList[i] + '<button class="removeDef" id="' +i  + '">x</button></li>';
    };
    html += '</ul>';
 
    document.getElementById('defList').innerHTML = html;
 
    var buttons = document.getElementsByClassName('removeDef');
    for (var i=0; i < buttons.length; i++) {
        buttons[i].addEventListener('click', removeDef);
    };
}

document.getElementById('addDef').addEventListener('click', addDef);

localStorage.removeItem('def');


