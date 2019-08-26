//FUNCIONES---------------------------------------------------------------------------------------------------

//INICIO FUNCION codigo de checkboxes de table
function filterTableChange(){
  if(democratCheck.checked){
    boxes[0] = "D";
  } else {
    boxes[0] = "";
  }

  if(republicanCheck.checked){
    boxes[1] = "R";
  } else {
    boxes[1] = "";
  }

  if(independentCheck.checked){
    boxes[2] = "I";
  } else {
    boxes[2] = "";
  }
  chargeMyTable(data, boxes)
}
//FIN FUNCION

//INICIO FUNCION para evitar datos repetidos 
function notRepeated(array1) {
  var repeatNumbersArray = [];
  for (h = 0; h <= array1.length -1; h++) {
    for (n=0; n <= repeatNumbersArray.length; n++){
      let positionArray = repeatNumbersArray.indexOf(array1[h]);
      if (positionArray <= -1){
        repeatNumbersArray.push(array1[h]);
      }
    }
  }
  return repeatNumbersArray;
}
//FIN FUNCION

//INICIO FUNCION y codigo de llenado de dropdown
function chargeMyDropdown(array) {
let HTML = "";
let arrayComplete = array.results[0].members;

let statesArray = [];
arrayComplete.forEach(function (person) {
statesArray.push(person.state)     // aca se carga un array con todos los estados, incluso repetidos
});

statesArray = notRepeated(statesArray);
statesArray.sort();
  HTML +=`<option value="all">All</option>
  ${statesArray.map(statesArray => `<option value="${statesArray}">${statesArray} </option>`).join("")}`
document.getElementById('dropdownFilter').innerHTML = HTML;
}
//FIN FUNCION

//INICIO FUNCION y codigo de llenado de tabla
function chargeMyTable(array) {


  let selectedState = document.getElementById("dropdownFilter").value
    var HTML = `<thead>
    <td>FULL NAME</td>
    <td>party</td>
    <td>state</td>
    <td>seniority</td>
    <td>percent of the votes</td>
    </thead>
    <tbody>`; //Genero una variable llamada HTML que es donde va a ir bueno, el HTML
    var arrayComplete = (array.results[0].members);
    arrayComplete.forEach(function (person) {
  
      //ignorar-------------------------
      if (person.party == "R"){
        var party = "Republican";
      }
      else if (person.party == "D"){
        var party = "Democrat";
      }
      else{
        var party = "Independent";
      }
      //--------------------------------
      
      //aca compruebo el estado de los checkboxes
        for(n=0; n<=2; n++){
          if ((boxes[n] == person.party) && (selectedState == person.state || selectedState == "all")) {
          
            HTML += `<tr>
            <th>
            <a href='${person.url}'>
            ${person.first_name}
            ${person.middle_name || ""}
            ${person.last_name}</a></th>
        
            <td>${party}</td>
            <td>${person.state}</td>
            <td>${person.seniority}</td>
            <td>${person.votes_with_party_pct}%</td>
            </tr>`;
    
          }
        }
    });
    HTML += `</tbody>`;
    document.getElementById('people-data').innerHTML = HTML; //Aca inyecto el HTML en la tabla
  }
//FIN FUNCION del codigo de llenado de table
//FINAL DE FUNCIONES--------------------------------------------------------------------------------------------

//MAIN CODE
var boxes = ["D","R","I"];

var democratCheck = document.getElementById('democratCheck');
democratCheck.addEventListener("change", filterTableChange);

var republicanCheck = document.getElementById('republicanCheck');
republicanCheck.addEventListener("change", filterTableChange);

var independentCheck = document.getElementById('independentCheck');
independentCheck.addEventListener("change", filterTableChange);

document.getElementById("dropdownFilter").onchange = function(){
  chargeMyTable(data)
}

chargeMyDropdown(data);

chargeMyTable(data);

