const app = new Vue({
    el: '#app',
    data: {
        actual_page: "game_list_page",
        list_players: [],
        list_games: [],
        user_active: false,
        user_information: {},
        creation_game: false
    },
    mounted: () => {
        //fetch a games
        fetch("/api/games")
            .then(function (response) {
                if (response.ok) {
                    return response.json()
                } else {
                    throw new error(response.status)
                }
            })
            .then(json => {
                app.list_games = json.games

            })
            .catch(function (error) {
                console.log(error)
            })
            //fetch a players
            fetch("/api/players")
            .then(function (response) {
                if (response.ok) {
                    return response.json()
                } else {
                    throw new error(response.status)
                }
            })
            .then(json => {
                app.list_players = json

            })
            .catch(function (error) {
                console.log(error)
            })
            //fetch a user conectado
            fetch("/api/connected")
            .then(res => {
                    return res.json()
            })
            .then(json => {
                app.user_information = json
                if (json.player != "guest"){
                    app.user_active = true
                }
                return json
            })
            .catch(function (Error) {
                console.log(Error)
            })   
    },
    methods:{
        logOut: function(ev){
            return fetch("/api/logout",{
                method: "Post"
            })
            .then(response =>{
                 if(response.status==200) {
                    alert("Se a desconectado correctamente")
                    this.user_active = false
                    }
                })
            },
        compruebaUser: function(){
            //fetch a connected (indica el usuario conectado)
            fetch("/api/connected")
            .then(res => {
                    return res.json()
            })
            .then(json => {
                app.user_information = json
                if (json.player != "guest"){
                    app.user_active = true
                }
                return json
            })
            .catch(function (Error) {
                console.log(Error)
            })
        },
    },
    components: {
        game_list_page: {
            methods:{
                registerGame: function(ev){
                    return fetch("/api/games",{
                        method: "Post"
                    })
                    .then(res =>{
                            return res.json()
                        })
                    .then(json =>{
                        alert(JSON.stringify(json))
                        window.location.reload(true);
                    })
                    
                },
                joinGame: function(gameId){
                    let url = "/api/joinGame/"
                    url += gameId
                    console.log(url)
                    return fetch(url,{
                        method: "Post"
                    })
                    .then(res =>{
                            return res.json()
                        })
                    .then(json =>{
                        alert(JSON.stringify(json))
                        window.location.reload(true);
                    })
                },
                goToGame: function(gpId){
                    let url = "/grid.html?gp=" + gpId + "&ships=1"

                }
            },
            props: ['games','userdata'],
            //PREGUNTAR A RODRI COMO HACER CON V-ELSE PARA MOSTRAR LA INFO A PESAR DE HABER 1 SOLO GP
            //ERROR CON app.user_information.player.user_name
            template: `
            {{app.compruebaUser}}
                        <div>
                            <div class="text-center">
                                <img id="imagenFondo" src="images/playstation.jpg" class="rounded">
                                <div>
                                    <nav style="border: 2px solid black; position: relative; z-index: 10; ">
                                        <ul style="list-style-type: none; display: flex; justify-content: space-around;">
                                            <li><button v-show="app.user_active" type="button" class="btn btn-primary"><a @click="registerGame">Create Game</a></button></li>
                                        </ul>
                                    </nav>
                                    </div>
                                <table class="table">
                                    <thead class="thead-dark">
                                        <tr>
                                            <th scope="col">Game ID</th>
                                            <th scope="col">Creation Date</th>
                                            <th scope="col">User Creator</th>
                                            <th scope="col">User Guest</th>
                                            <th scope="col">Join Time</th>
                                            <th scope="col">Join Game</th>
                                            <th scope="col">Go to the game</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr v-for="game in games">
                                            <th scope="row">{{game.game_id}}</th>
                                            <td >{{game.creation_date}}</td>
                                            <td v-for="gp in game.game_players" v-if="gp.is_host"> {{gp.player.user_name}} </td>
                                            <td v-for="gp in game.game_players" v-if="gp.is_host == false"> {{gp.player.user_name}} </td>
                                            <td v-for="gp in game.game_players" v-if="gp.is_host == false"> {{gp.join_time}} </td>

                                            <td v-for="gp in game.game_players" v-show="app.user_active == true && game.game_players.length == 1" >
                                                <button type="button" class="btn btn-primary"
                                                v-if="gp.player.user_name != userdata.player.user_name">
                                                    <a @click="joinGame(game.game_id)"> Join Game </a>
                                                </button>
                                            </td>

                                            <td v-for="gp in game.game_players" v-show="app.user_active == true" >
                                                <button type="button" class="btn btn-primary"
                                                v-if="gp.player.user_name == userdata.player.user_name">
                                                    <a :href="'/grid.html?gp='+gp.game_player_id+'&ships=1'"> Go to the game </a>
                                                </button>
                                            </td>
                                            
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        `
        },
        player_boardscore_page: {
            data: function(){
                return {
                    scores: []
                }
            },
            created() {
                this.calculate(this.players)
            },
            methods: {
                calculate: function(players){

                    players.forEach(player => {
                        let obj = {}
                        obj.id = player.player_id
                        obj.username = player.user_name
                        obj.win = 0
                        obj.total = 0
                        obj.lost = 0
                        obj.tied = 0

                        player.game_players.forEach(gp =>{
                            if (gp.score == 0.0){
                                obj.lost ++
                            }else if (gp.score == 0.5){
                                obj.tied ++
                            }else if (gp.score == 1.0){
                                obj.win ++
                            }  
                        })
                        obj.total = obj.win + (obj.tied/2)
                        this.scores.push(obj)
                    });
                }
            },
            props: ['players'],
            template: `
                        <div>
                            <div class="text-center">
                                <img id="imagenFondo" src="images/playstation.jpg" class="rounded">

                                <table class="table">
                                    <thead class="thead-dark">
                                        <tr>
                                            <th scope="col">User ID</th>
                                            <th scope="col">User Name</th>
                                            <th scope="col">Total</th>
                                            <th scope="col">Won</th>
                                            <th scope="col">Lost</th>
                                            <th scope="col">Tied</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr v-for="score in scores">
                                            <th scope="row">{{score.id}}</th>
                                            <td>{{score.username}}</td>
                                            <td>{{score.total}}</td>
                                            <td> {{score.win}} </td>
                                            <td> {{score.lost}} </td>
                                            <td> {{score.tied}} </td>
                                        </tr>
                                    </tbody>
                                </table>

                            </div>
                        </div>
                        `
        },
        login_page: {
            props: ["user_active"],
            data: function(){
                return {
                    login: true,
                    register: false
                }
            },
             methods: {
                log: function(ev){
                    let form = ev.target
                    let result = true
                    if (form.username.value == "" || form.password.value == "") {
                        alert("flaco, llename TODOS los campos")
                        return false
                    }else{
                        //fetch a LOGIN
                        let formdata = new FormData();
                        formdata.append("username", form.username.value)
                        formdata.append("password", form.password.value)
                        return fetch("/api/login",{
                            method: "Post",
                            body: formdata
                        })
                        .then(response =>{
                            //NO RESOLVER LA RESPUESTA, LOGUEO MANEJADO POR SPRING, NO ES POSIBLE MANEJAR EL JSON
                            if (response.status==401) {
                                alert("Error en las credenciales. No son correctas")
                            }else if(response.status==200) {
                                alert("Se a conectado exitosamente || " + "El usuario se llama: " + form.username.value )
                                this.login= false
                                app.user_active = true
                            }
                        })
                        .catch(function (error) {
                            console.log(error)
                        })
                    }
                },
                registration: function(ev){
                    let form = ev.target
                    if (form.username.value == "" || form.password.value == "" || form.email.value == "") {
                        alert("flaco, llename TODOS los campos")
                        return false
                    }else{
                        //fetch a LOGIN
                        let formdata = new FormData();
                        formdata.append("username", form.username.value)
                        formdata.append("password", form.password.value)
                        formdata.append("email", form.email.value)
                        return fetch("/api/players",{
                            method: "Post",
                            body: formdata
                        })
                        .then(response =>{
                            if (response.status==403) { //ERROR, FORBIDDEN
                                return response.json()
                            }else if(response.status==201) { //SUCCESS, Registrado
                                this.login= true
                                this.register= false
                                return response.json()
                            }
                        })
                        .then(json =>{
                            alert(JSON.stringify(json))
                        })
                        .catch(function (error) {
                            console.log(error)
                        })
                    }
                }

            },
            template: `
                        <div>
                            <div class="sidenav">
                                <div class="login-main-text">
                                    <h2>Application<br> Login Page</h2>
                                    <p>Login or register from here to access.</p>
                                </div>
                            </div>

                            <div v-if="user_active == false && login == true" class="main">
                                <div class="col-md-6 col-sm-12">
                                    <div class="login-form">
                                        <form v-on:submit.prevent="log($event)">
                                            <div class="form-group">
                                                <label>User Name</label>
                                                <input type="text" class="form-control" placeholder="User Name" name="username">
                                            </div>
                                            <div class="form-group">
                                                <label>Password</label>
                                                <input type="password" class="form-control" placeholder="Password" name="password">
                                            </div>
                                            <button type="submit" class="btn btn-black">Log in</button>
                                            <button v-show="user_active == false" v-on:click="register = true, login = false" type="button" class="btn btn-black">Register</button>
                                        </form>
                                    </div>
                                </div>
                            </div>

                        <div v-if="register" class="main">
                            <div class="col-md-6 col-sm-12">
                                <div class="login-form">
                                    <form v-on:submit.prevent="registration($event)">
                                        <div class="form-group">
                                            <label>User Name</label>
                                            <input type="text" class="form-control" placeholder="User Name" name="username">
                                        </div>
                                        <div class="form-group">
                                            <label>Password</label>
                                            <input type="password" class="form-control" placeholder="Password" name="password">
                                        </div>
                                        <div class="form-group">
                                            <label>E-mail</label>
                                            <input type="email" class="form-control" placeholder="Email" name="email">
                                        </div>
                                        <button type="submit" class="btn btn-secondary">Register</button>
                                        <button v-on:click="login = true, register = false" type="button" class="btn btn-black">Log in</button>
                                    </form>
                                </div>
                            </div>
                        </div>

                    </div>
                    `
                    }
                }
            })