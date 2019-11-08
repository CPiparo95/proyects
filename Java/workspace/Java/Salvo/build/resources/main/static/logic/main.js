const app = new Vue({
    el: '#app',
    data: {
        actual_page: "game_list_page",
        list_players: [],
        list_games: [],
    },
    created: () => {
        
        if (window.location.href == "http://localhost:8080/index.html") {  //fetch al java
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
        }
        else if(window.location.href == "http://localhost:8080/player_leaderboard_score.html") {
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
        }
        
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
            template: `
            <div class="container login-container">
            <div class="row">
                <div class="col-md-6 login-form-2">
                    <h3>Log in</h3>
                    <form>
                        <div class="form-group">
                            <input type="text" class="form-control" placeholder="Your Email *" value="" />
                        </div>
                        <div class="form-group">
                            <input type="password" class="form-control" placeholder="Your Password *" value="" />
                        </div>
                        <div class="form-group">
                            <input type="submit" class="btnSubmit" value="Login" />
                        </div>
                        <div class="form-group">
                            <a href="#" class="ForgetPwd" value="Login">Forget Password?</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
                        `
        },
    }
})