const app = new Vue({
    el: '#app',
    data: {
        actual_page: "game_list_page",
        list_players: [],
        list_games: [],
    },
    created: () => {
        //fetch al java
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
        
    },
    components: {
        game_list_page: {
            props: ['games'],
            template: `
                        <div>
                            <div class="text-center">
                                <img id="imagenFondo" src="images/playstation.jpg" class="rounded">

                                <table class="table">
                                    <thead class="thead-dark">
                                        <tr>
                                            <th scope="col">Game ID</th>
                                            <th scope="col">Game Name</th>
                                            <th scope="col">Creation Date</th>
                                            <th scope="col">User Creator</th>
                                            <th scope="col">User Guest</th>
                                            <th scope="col">Join Time</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr v-for="game in games">
                                            <th scope="row">{{game.game_id}}</th>
                                            <td>{{game.game_name}}</td>
                                            <td v-for="gp in game.game_players" v-if="gp.is_host"> {{gp.join_time}} </td>
                                            <td v-for="gp in game.game_players" v-if="gp.is_host"> {{gp.player.user_name}} </td>
                                            <td v-for="gp in game.game_players" v-if="gp.is_host == false"> {{gp.player.user_name}} </td>
                                            <td v-for="gp in game.game_players" v-if="gp.is_host == false"> {{gp.join_time}} </td>
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

                        player.game_player.forEach(gp =>{
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
            props: [],
            data: function(){
                return {
                    chicho: "",
                    perro: ""
                }
            },
             methods: {
                register: function(ev){
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
                        .then(function (response) {
                            //if (response.ok) {
                                return response.json()
                            //} else {
                               // throw new Error(response.status)
                            //}
                        })
                        .then(json => {
                            alert(json)
            
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
                            <div class="main">
                                <div class="col-md-6 col-sm-12">
                                    <div class="login-form">
                                        <form v-on:submit.prevent="register($event)">
                                            <div class="form-group">
                                                <label>User Name</label>
                                                <input type="text" class="form-control" placeholder="User Name" name="username">
                                            </div>
                                            <div class="form-group">
                                                <label>Password</label>
                                                <input type="password" class="form-control" placeholder="Password" name="password">
                                            </div>
                                            <button type="submit" class="btn btn-black">Log in</button>
                                            <button type="submit" class="btn btn-secondary">Register</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    `
                    },
                }
            })