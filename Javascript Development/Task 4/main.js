//INICIO FUNCION trae datos fetch
function traerDatos() {
    let sPath = window.location.pathname;
    let sPage = (sPath.substring(sPath.lastIndexOf('/') + 1)).split('-');
    let sname = sPage[0];
    console.log(sname); //devuelve si es "Senate" o "house"
    //Generate Header
    let myHeaders = new Headers();
    myHeaders.append('X-API-Key', 'hgAkxR7fIwUj9BOTbv51ZCyXGlVXvV134mqIrctH'); // <- Append Personal Key from Congress
    var init = {
        metho: 'GET',
        headers: myHeaders,
        mode: 'cors'
    }
    let myRequest = `https://api.propublica.org/congress/v1/113/${sname}/members.json`; // <- URL
    fetch(myRequest, init)
        .then(function (response) { //One then to get response
            if (response.ok) {
                return response.json();
            }
            //Signal a server error to the chain
            throw new Error(response.statusText);
        })
        .then(function (json) { //If OK -> Get data
            data = json;
            let dataFunctions = data.results[0].members;
            table.dataTable = dataFunctions;
        })
        .catch(function (error) { //IF ERROR -> CATCH
            console.log("Request has failed: " + error.message);
        })
} //FIN FUNCION

//vue.js testing
var table = new Vue({
    el: '#table',
    data: {
        dataTable: [],
        parties: ["D", "R", "I"],
        states: "all"
    },
    methods: {
        notRepeated(array1) {
            var repeatNumbersArray = [];
            for (h = 0; h <= array1.length - 1; h++) {
                for (n = 0; n <= repeatNumbersArray.length; n++) {
                    let positionArray = repeatNumbersArray.indexOf(array1[h]);
                    if (positionArray <= -1) {
                        repeatNumbersArray.push(array1[h]);
                    }
                }
            }
            return repeatNumbersArray;
        },
        
        fullParty(party) {
            if (party == "R") {
                return "Republican";
            } else if (party == "D") {
                return "Democrat";
            } else {
                return "Independent";
            }
            //---
        }
    },
    computed: {
        filterMembers() {
            return this.dataTable.filter(e => ((table.parties.includes(e.party)) && (this.states == e.state || this.states == "all" )) ? e : null);
        },
        statesArray(){
            let aux = []
            this.dataTable.forEach(e => !aux.includes(e.state) ? aux.push(e.state) : null)
            return aux.sort()
        },
    }
})

traerDatos()