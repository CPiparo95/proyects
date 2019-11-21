/*creates the ships with the ability of been placed in the grid. 
It requires a shipType, that is, the id by wich the ship will be recongnized;
the amount of cells the ship is going to occupy in the grid;
a parent where the ship will be appended to;
and a boolean that specifies whether the ship can be moved or not.
*/
const createShips = function (shipType, length, orientation, parent, isStatic) {

    let ship = document.createElement('DIV')
    let grip = document.createElement('DIV')
    let content = document.createElement('DIV')

    ship.classList.add('grid-item')
    ship.dataset.length = length
    ship.dataset.orientation = orientation
    ship.id = shipType

    if (orientation == 'vertical') {
        ship.style.transform = 'rotate(90deg)'
    }

    if (window.innerWidth >= 768) {
        ship.style.width = `${length * 45}px`
        ship.style.height = '45px'
    } else if (window.innerWidth >= 576) {
        ship.style.width = `${length * 35}px`
        ship.style.height = '35px'
    } else {
        ship.style.width = `${length * 30}px`
        ship.style.height = '30px'
    }

    window.addEventListener('resize', () => {
        if (window.innerWidth >= 768) {
            ship.style.width = `${length * 45}px`
            ship.style.height = '45px'
        } else if (window.innerWidth >= 576) {
            ship.style.width = `${length * 35}px`
            ship.style.height = '35px'
        } else {
            ship.style.width = `${length * 30}px`
            ship.style.height = '30px'
        }
    })

    if (!isStatic) {
        grip.classList.add('grip')
        grip.draggable = 'true'
        grip.addEventListener('dragstart', dragShip)
        ship.addEventListener('touchmove', touchShip)
        ship.addEventListener('touchend', touchShipEnd)
        ship.appendChild(grip)
    }


    content.classList.add('grid-item-content')
    ship.appendChild(content)

    parent.appendChild(ship)

    if (!isStatic) {
        rotateShips(shipType)
    } else {
        checkBusyCells(ship, parent)
    }



    //event to allow the ship beeing dragged
    function dragShip(ev) {
        ev.dataTransfer.setData("ship", ev.target.parentNode.id)

    }

    //event to allow the ship beeing dragged on touch devices
    function touchShip(ev) {
        // make the element draggable by giving it an absolute position and modifying the x and y coordinates
        ship.classList.add("absolute");

        var touch = ev.targetTouches[0];
        // Place element where the finger is
        ship.style.left = touch.pageX - 25 + 'px';
        ship.style.top = touch.pageY - 25 + 'px';
        event.preventDefault();
    }

    function touchShipEnd(ev) {
        // hide the draggable element, or the elementFromPoint won't find what's underneath
        ship.style.left = '-1000px';
        ship.style.top = '-1000px';
        // find the element on the last draggable position
        var endTarget = document.elementFromPoint(
            event.changedTouches[0].pageX,
            event.changedTouches[0].pageY
        );


        // position it relative again and remove the inline styles that aren't needed anymore
        ship.classList.remove('absolute')
        ship.style.left = '';
        ship.style.top = '';
        // put the draggable into it's new home
        if (endTarget.classList.contains('grid-cell')) {
            let y = endTarget.dataset.y.charCodeAt() - 64
            let x = parseInt(endTarget.dataset.x)
            if (ship.dataset.orientation == 'horizontal') {
                if (parseInt(ship.dataset.length) + x > 11) {
                    document.querySelector("#display p").innerText = 'movement not allowed'
                    return
                }
                for (let i = 1; i < ship.dataset.length; i++) {
                    let id = (endTarget.id).match(new RegExp(`[^${endTarget.dataset.y}|^${endTarget.dataset.x}]`, 'g')).join('')
                    let cellId = `${id}${endTarget.dataset.y}${x + i}`
                    if (document.getElementById(cellId).className.search(/busy-cell/) != -1) {
                        document.querySelector("#display p").innerText = 'careful'
                        return
                    }
                }
            } else {
                if (parseInt(ship.dataset.length) + y > 11) {
                    document.querySelector("#display p").innerText = 'movement not allowed'
                    return
                }
                for (let i = 1; i < ship.dataset.length; i++) {
                    let id = (endTarget.id).match(new RegExp(`[^${endTarget.dataset.y}|^${endTarget.dataset.x}]`, 'g')).join('')
                    let cellId = `${id}${String.fromCharCode(endTarget.dataset.y.charCodeAt() + i)}${x}`
                    if (document.getElementById(cellId).className.search(/busy-cell/) != -1) {
                        document.querySelector("#display p").innerText = 'careful'
                        return
                    }
                }
            }
            endTarget.appendChild(ship);
            ship.dataset.x = x
            ship.dataset.y = String.fromCharCode(y + 64)

            checkBusyCells(ship, endTarget)
        } else {
            document.querySelector("#display p").innerText = 'movement not allowed'
            return
        }
    }

    //event to allow the ship rotation
    function rotateShips(shipType) {

        document.querySelector(`#${shipType}`).addEventListener('click', function (ev) {

            document.querySelector("#display p").innerText = ''

            let ship = ev.target.parentNode
            let orientation = ship.dataset.orientation
            let cell = ship.parentElement.classList.contains('grid-cell') ? ship.parentElement : null

            if (cell != null) {
                if (orientation == 'horizontal') {
                    if (parseInt(ship.dataset.length) + (cell.dataset.y.charCodeAt() - 64) > 11) {
                        document.querySelector("#display p").innerText = 'careful'
                        return
                    }

                    for (let i = 1; i < ship.dataset.length; i++) {
                        let id = (cell.id).match(new RegExp(`[^${cell.dataset.y}|^${cell.dataset.x}]`, 'g')).join('')
                        let cellId = `${id}${String.fromCharCode(cell.dataset.y.charCodeAt() + i)}${cell.dataset.x}`
                        if (document.getElementById(cellId).className.search(/busy-cell/) != -1) {
                            document.querySelector("#display p").innerText = 'careful'
                            return
                        }
                    }

                } else {
                    if (parseInt(ship.dataset.length) + parseInt(cell.dataset.x) > 11) {
                        document.querySelector("#display p").innerText = 'careful'
                        return
                    }

                    for (let i = 1; i < ship.dataset.length; i++) {
                        let id = (cell.id).match(new RegExp(`[^${cell.dataset.y}|^${cell.dataset.x}]`, 'g')).join('')
                        let cellId = `${id}${cell.dataset.y}${parseInt(cell.dataset.x) + i}`
                        if (document.getElementById(cellId).className.search(/busy-cell/) != -1) {
                            document.querySelector("#display p").innerText = 'careful'
                            return
                        }
                    }
                }
            }

            if (orientation == 'horizontal') {
                ship.dataset.orientation = 'vertical'
                ship.style.transform = 'rotate(90deg)'

            } else {
                ship.dataset.orientation = 'horizontal'
                ship.style.transform = 'rotate(360deg)'

            }
            if (cell != null) {
                checkBusyCells(ship, cell)
            }

        })
    }
}

let data = {}
var params = new URLSearchParams(window.location.search)
let salvoArray = []
var ships = ['gaucho1', 'gaucho2', 'gaucho3', 'gaucho4', 'gaucho5'];
var shipsLocated = [];
document.getElementById("collect-boats").addEventListener("click", function () {
    getShips();
});

function getShips() {
    for (let i = 0; i < ships.length; i++) {
        let shipObject = {
            type: "",
            locations: []
        }
        let ship = document.getElementById(ships[i]);
        if (ship.dataset.y != undefined && ship.dataset.x != undefined) {
            shipObject.type = ship.id;

            let config = document.getElementsByClassName(`${ships[i]}-busy-cell`);
            for (let i = 0; i < config.length; i++) {
                shipObject.locations.push(config[i].dataset.y + config[i].dataset.x);
            }
            shipsLocated.push(shipObject);
        }
    }
    sendToBackend()
}

function sendToBackend() {
    if(shipsLocated.length==5){
        fetch("/api/placeShips/" + params.get("gp"), {
                method: 'POST',
                body: JSON.stringify(shipsLocated),
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response=> {
                return response.json();
            })
            .then(json => {
                alert(JSON.stringify(json));
                
                window.location.reload(true)
            })
            .catch(ex => console.log(ex));
    }
}

window.addEventListener("load", function(){
    let salvoCells = document.querySelectorAll("#salvoGrid .grid-cell");
    salvoCells.forEach(element => {
        element.addEventListener("click", function(evt){
          let clicked = evt.currentTarget;
          let location = clicked.dataset.y + clicked.dataset.x;
          if (app.salvoesPositionsFire.includes(location) || (app.salvoesPositionsNotFire.includes(location))) {
            alert("La posicion ya fue ingresada");
          } else if (app.salvoesPositionsNotFire.length == 5) {
            alert("ya disparaste todos tus tiros, si quieres cambiar las posiciones recarga la pagina.");
          }else if (app.shipsPositioned == false){
            alert("Todabia no es fase de disparos.");
          }else{
              document.getElementById("salvo" + location).style.backgroundColor="red"
              app.salvoesPositionsNotFire.push(location);
          }
      });
    });
  })

  function sendSalvoes() {
    if(app.salvoesPositionsNotFire.length==5){
        let salvoObject = {
            locations: []
        }
        salvoObject.locations = app.salvoesPositionsNotFire

        fetch("/api/fireSalvoes/" + params.get("gp"), {
                method: 'POST',
                body: JSON.stringify(salvoObject),
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response=> {
                return response.json();
            })
            .then(json => {
                alert(JSON.stringify(json));
                app.salvoesFired = true
                window.location.reload(true)
            })
            .catch(ex => console.log(ex));
    }else{
        alert("se va a caer, digo, tenes que mandar 5 salvos, ni mas ni menos")
    }
}

//fetch para cargar tanto salvos como barcos, tambien indica la fase y el turno
fetch("/api/game_view/" + params.get("gp"))
    .then(function (response) {
        return response.json()
    })
    .then(json => {
        console.log(json)
        data = json.data

        //DETECTA EL TURNO EN BASE A LA CANTIDAD DE SALVOS Y LO GUARDA EN app.turno
        let parImpar = (data.salvoes.length %2)? "impar":"par"
        if (parImpar == "par") {
            app.turno = (data.salvoes.length / 2) + 1
        }else{
            app.turno = ((data.salvoes.length - 1) / 2) + 1
        }

        let 
        for (n = 0; n <= data.game_players.length - 1; n++) {
            if (data.game_players[n].game_player_id == params.get("gp")) {
                data.game_players[n].sinks.forEach(sink => {
                    
                });
            }
        }

            //ESTE FOR GIGANTE MANDA LOS SALVOS QUE NOSOTROS DISPARAMOS CON ANTERIORIDAD
            for (n = 0; n <= data.game_players.length - 1; n++) { //este for consulta el nombre del jugador
                if (data.game_players[n].game_player_id == params.get("gp")) {

                for (h = 0; h <= data.salvoes.length - 1; h++) { //este for comprueba que estemos en los salvos del jugador correcto
                        if (data.game_players[n].player.user_name == data.salvoes[h].player_username) {
                            for (l = 0; l <= data.salvoes[h].fire_positions.length - 1; l++) { //finalmente crea salvoes por cada jugador
                                data.salvoes[h].hits.forEach(hit => {
                                    if(hit == data.salvoes[h].fire_positions[l]){
                                        let shot = document.createElement("img");
                                shot.setAttribute("src", "assets/ships/explosion.gif");
                                shot.style.zIndex = 10;
                                shot.style.width = "30px";
                                shot.style.height = "30px";
                                shot.style.margin = "2.5px";
                                shot.style.position = "absolute";
                                document.getElementById("salvo" + data.salvoes[h].fire_positions[l]).appendChild(shot);
                                app.salvoesPositionsFire.push(data.salvoes[h].fire_positions[l])
                                if(app.salvoesPositionsFire.length >= 5*(app.turno+1)){
                                    app.salvoesFired = true
                                }
                                    }else{
                                        let shot = document.createElement("img");
                                        shot.setAttribute("src", "assets/ships/explosion_agua.jpg");
                                        shot.style.zIndex = 10;
                                        shot.style.width = "30px";
                                        shot.style.height = "30px";
                                        shot.style.margin = "2.5px";
                                        shot.style.position = "absolute";
                                        document.getElementById("salvo" + data.salvoes[h].fire_positions[l]).appendChild(shot);
                                        app.salvoesPositionsFire.push(data.salvoes[h].fire_positions[l])
                                        if(app.salvoesPositionsFire.length >= 5*(app.turno+1)){
                                            app.salvoesFired = true
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
            //CARGA LOS BARCOS
            if(data.ships.length == 5){
                app.shipsPositioned = true //CAMBIA EL TITULO, osea, LA FASE
                data.ships.forEach(item => { //CARGA LOS BARCOS EN LA GRILLA
                    if (item.ship_positions[0].slice(1) == item.ship_positions[1].slice(1)){
                        createShips(item.ship_type, item.ship_positions.length,
                        'vertical', document.getElementById('ships' + item.ship_positions[0]), true)
                    }else{
                        if (item.ship_positions[0].length > 2){
                            createShips(item.ship_type, item.ship_positions.length,
                            'horizontal', document.getElementById('ships' + item.ship_positions[1]), true)
                        }
                        createShips(item.ship_type, item.ship_positions.length,
                        'horizontal', document.getElementById('ships' + item.ship_positions[0]), true)
                    }
                    
                })
                //CARGA LOS SALVOS QUE NOS TIRARON A NOSOTROS
                for (n = 0; n <= data.game_players.length - 1; n++) { //este for consulta el nombre del jugador
                    if (data.game_players[n].game_player_id != params.get("gp")) {
                        for (h = 0; h <= data.salvoes.length - 1; h++) { //este for comprueba que estemos en los salvos del jugador correcto
                            if (data.game_players[n].player.user_name == data.salvoes[h].player_username) {
                                for (l = 0; l <= data.salvoes[h].fire_positions.length - 1; l++) { //finalmente crea salvoes por cada jugador
                                    let shot = document.createElement("img");
                                    shot.setAttribute("src", "assets/ships/explosion.gif");
                                    shot.style.zIndex = 10;
                                    shot.style.width = "30px";
                                    shot.style.height = "30px";
                                    shot.style.margin = "2.5px";
                                    shot.style.position = "absolute";
                                    document.getElementById("ships" + data.salvoes[h].fire_positions[l]).appendChild(shot);
                                    }
                                }
                            }
                        }
                    }
                    //CARGA LOS BARCOS EN CASO DE QUE NO LOS HAYAMOS COLOCADO
                }else{
                    createShips('gaucho1', 5, 'horizontal', document.getElementById('dock'),false)
                    createShips('gaucho2', 4, 'horizontal', document.getElementById('dock'),false)
                    createShips('gaucho3', 3, 'horizontal', document.getElementById('dock'),false)
                    createShips('gaucho4', 3, 'horizontal', document.getElementById('dock'),false)
                    createShips('gaucho5', 2, 'horizontal', document.getElementById('dock'),false)
                }
        })
    .catch(function (error) {
        console.log(error)
    })