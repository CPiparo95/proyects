//FUNCIONES----------------------------------------------------------------------------------------------------

//constante con los datos a procesar
const statistics_data = {
    //datos escenciales
    democrats: 0,
    republicans: 0,
    independents: 0,
    //datos para table glance
    senate_glance: {
        democrats_votes: 0,
        republican_votes: 0,
        independent_votes: 0
    },
    //datos para tabla most/least engaged
    general_votes: [],
    //datos para tabla least engaged
    least_general_votes: [],
    //datos para tabla most engaged 
    most_general_votes: []
}

//INICIO FUNCION procesa datos escenciales
function processEscencialData(members) {
    for (n = 0; n <= members.length - 1; n++) {
        switch (members[n].party) {
            case "D":
                statistics_data.democrats++;
                continue;
            case "R":
                statistics_data.republicans++;
                continue;
            case "I":
                statistics_data.independents++;
        }
    }
} //FIN FUNCION

//INICIO FUNCION devuelve el nombre de la tabla en uso
function nombreTablaGlance() {

    if (document.getElementById("glance-senate-loyalty")) {
        table_name = "glance-senate-loyalty"
    } else if (document.getElementById("glance-senate-attendance")) {
        (table_name = "glance-senate-attendance")
    } else {
        alert("ERROR CRITICO, NO EXISTE TABLA GLANCE O ID INCORRECTO")
    }
    return table_name
} //FIN FUNCION

//INICIO FUNCION procesa datos para tablas GLANCE
function processDataGlance(members) {
    let democrats_votes = 0;
    let independent_votes = 0;
    let republican_votes = 0;

    //LOYALTY
    //suma de los porcentajes existentes
    //luego dividirlos por cantidad de politicos segun partido
    //ATTENDANCE
    //por cada politico, suma cantidad de votos a partir de su periodo y resta votos perdidos,
    //luego dividirlos por cantidad de politicos segun partido

    if (nombreTablaGlance() == "glance-senate-loyalty") {
        for (n = 0; n <= members.length - 1; n++) {
            switch (members[n].party) {
                case "D":
                    democrats_votes += members[n].votes_with_party_pct;
                    continue;
                case "R":
                    republican_votes += members[n].votes_with_party_pct;
                    continue;
                case "I":
                    independent_votes += members[n].votes_with_party_pct;
                    continue;
            }
        }
    } else {
        // ahora estamos en attendance
        for (n = 0; n <= members.length - 1; n++) {
            switch (members[n].party) {
                case "D":
                    democrats_votes += (members[n].total_votes - members[n].missed_votes);
                    continue;
                case "R":
                    republican_votes += (members[n].total_votes - members[n].missed_votes);
                    continue;
                case "I":
                    independent_votes += (members[n].total_votes - members[n].missed_votes);
                    continue;
            }
        }
    }
    statistics_data.senate_glance.democrats_votes = democrats_votes / statistics_data.democrats;
    statistics_data.senate_glance.republican_votes = republican_votes / statistics_data.republicans;
    statistics_data.senate_glance.independent_votes = independent_votes / statistics_data.independents;
} //FIN FUNCION

//INICIO FUNCION carga de datos a tabla GLANCE
function chargeTableGlance() {

    let HTML = `<thead>
    <td>PARTY</td>
    <td>Number of representatives</td>
    <td> % voted with party</td>
    </thead>
    <tbody>
    <tr>
    <td>Republican</td>
    <td>${statistics_data.republicans}</td>
    <td>${statistics_data.senate_glance.republican_votes}</td>
    </tr>
    <tr>
    <td>Democrat</td>
    <td>${statistics_data.democrats}</td>
    <td>${statistics_data.senate_glance.democrats_votes}</td>
    </tr>
    <tr>
    <td>Independent</td>
    <td>${statistics_data.independents || "N/H"}</td>
    <td>${statistics_data.senate_glance.independent_votes || "N/H"}</td>
    </tr>
    </tbody>`;
    document.getElementById(nombreTablaGlance()).innerHTML = HTML;

} //FIN FUNCION

//INICIO FUNCION para redondear
function redondeo(numero) {
    return Math.round(numero);
} //FIN FUNCION

//INICIO FUNCION procesa datos para tablas most/less
function processMostLessEngaged(members) {
    let total_votes = 0;
    let real_votes = 0;
    let oneMember = {
        name: "",
        link: "",
        partial_votes: 0,
        votes: 0
    };
    let tenPercent = ((members.length * 10) / 100);
    tenPercent = redondeo(tenPercent);

    if (!nombreTablaGlance() == "glance-senate-loyalty") {
        for (n = 0; n <= members.length - 1; n++) {
            //ATTENDANCE CASE
            total_votes = members[n].total_votes;
            real_votes = total_votes - members[n].missed_votes;
            oneMember.votes = ((real_votes / total_votes) * 100);
            //both cases
            oneMember.name =
                (members[n].first_name + " " + (members[n].middle_name || "") + " " + members[n].last_name);
            oneMember.link = members[n].url;
            oneMember.partial_votes = members[n].missed_votes;
            statistics_data.general_votes.push({
                name: oneMember.name,
                votes: oneMember.votes,
                link: oneMember.link,
                partial_votes: oneMember.partial_votes
            });
        }
    } else {
        //LOYALTY CASE
        for (n = 0; n <= members.length - 1; n++) {
            //ATTENDANCE CASE
            total_votes = (members[n].votes_with_party_pct + members[n].missed_votes_pct);
            real_votes = members[n].votes_with_party_pct;
            oneMember.votes = ((real_votes / total_votes) * 100);
            //both cases
            oneMember.name =
                (members[n].first_name + " " + (members[n].middle_name || "") + " " + members[n].last_name);
            oneMember.link = members[n].url;
            oneMember.partial_votes = members[n].missed_votes_pct;
            statistics_data.general_votes.push({
                name: oneMember.name,
                votes: oneMember.votes,
                link: oneMember.link,
                partial_votes: oneMember.partial_votes
            });
        }
    }
    //sort de general votes
    statistics_data.general_votes.sort(function (a, b) {
        if (a.votes > b.votes) {
            return 1;
        }
        if (a.votes < b.votes) {
            return -1;
        }
        // a must be equal to b
        return 0;
    });

    //LEAST CASE
    for (n = 0; n <= tenPercent - 1; n++) {
        statistics_data.least_general_votes.push(statistics_data.general_votes[n]);
    }
    for (n = tenPercent; n <= members.length - 1; n++) {
        if (statistics_data.least_general_votes[n - 1] == statistics_data.general_votes[n]) {
            statistics_data.least_general_votes.push(statistics_data.general_votes[n + 1]);
        } else {
            break;
        }
    }
    //MOST CASE
    for (n = members.length - 1; n >= (members.length - (tenPercent + 1)); n--) {
        statistics_data.most_general_votes.push(statistics_data.general_votes[n]);
    }
    for (n = (members.length - (tenPercent + 1)); n >= 0; n--) {
        if (statistics_data.most_general_votes[statistics_data.most_general_votes.length - 1] == statistics_data.general_votes[n - 1]) {
            statistics_data.most_general_votes.push(statistics_data.general_votes[n - 1]);
        } else {
            break;
        }
    }
} //FIN FUNCION

//INICIO FUNCION carga los datos a tablas most/least
function chargeTablesMostLeast() {
    let HTML = `<thead>
    <td>Name</td>`;
    if ((!nombreTablaGlance() == "glance-senate-loyalty")) {
        HTML += `<td>Number of Missed Votes</td>
    <td> % votes </td>
    </thead>
    <tbody>`;
    } else {
        HTML += `<td>Number of Party Votes</td>
        <td> % party votes </td>
        </thead>
        <tbody>`;
    }
    let HTMLmost = HTML;
    //CHARGE LEAST TABLE
    for (n = 0; n <= statistics_data.least_general_votes.length - 1; n++) {
        HTML += `<tr>
        <td><a href='${statistics_data.least_general_votes[n].link}'>
        ${statistics_data.least_general_votes[n].name}</a>
        </td>
        <td>${statistics_data.least_general_votes[n].partial_votes}</td>
        <td>${statistics_data.least_general_votes[n].votes}</td>
        </tr>
        </tbody>`;
    }
    document.getElementById("least").innerHTML = HTML;
    //CHARGE MOST TABLE
    for (n = 0; n <= statistics_data.most_general_votes.length - 1; n++) {
        HTMLmost += `<tr>
            <td><a href='${statistics_data.most_general_votes[n].link}'>
            ${statistics_data.most_general_votes[n].name}</a>
            </td>
            <td>${statistics_data.most_general_votes[n].partial_votes}</td>
            <td>${statistics_data.most_general_votes[n].votes}</td>
            </tr>
            </tbody>`;
    }
    document.getElementById("most").innerHTML = HTMLmost;
} //FIN FUNCION

//FINAL FUNCIONES---------------------------------------------------------------------------------------------

//datos del json del senate (el que tenga como data linkeado desde script)
let dataTable = data.results[0].members;

processEscencialData(dataTable);
processDataGlance(dataTable);
chargeTableGlance();
processMostLessEngaged(dataTable);
chargeTablesMostLeast();

//chargeTable(dataTable);