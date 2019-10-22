//get elements
const preObject = document.getElementById('object');

//create reference
const dbRefObject = firebase.database().ref('partidos/').child('ID1');

//Sync Object changes
dbRefObject.on('value', snap => console.log(snap.val()));

//SET de valores, FORMATEA OBJETO y le da nueva informacion, ELIMINANDO COMPLETAMENTE LA ANTERIOR
/* firebase.database().ref('partidos/').child('ID1').set({
    joputa : "joputa1",
    chicho : "chicho 2",
    perro : "soy un perro bueno",
    soyUnNumer : 123456
}) */

// UPDATE de valores, genera un nuevo hijo con valores, NO ELIMINA NADA
/* function writeNewPost() {
    // A post entry.
    var postData = {
        asistenciaLocal: 0,
        asistenciaVisitante: 0,
        equipoLocal: "Rodrigo",
        equipoVisitante: "Tadeo",
        fecha: "24-09-19",
        horario: "08:30",
        lugar: "Colegiales",
        resultado: "0-0"
        
    };
  
    // Get a key for a new Post.
    var newPostKey = firebase.database().ref('partidos/').push().key;
  
    // Write the new post's data simultaneously in the posts list and the user's post list.
    var updates = {};
    updates['/partidos/' + "ID10"] = postData;
  
    return firebase.database().ref().update(updates);
  } 
  */