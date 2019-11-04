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
                app.list_games = json

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
                                            <td v-for="gp in game.gamePlayers" v-if="gp.is_host"> {{gp.join_time}} </td>
                                            <td v-for="gp in game.gamePlayers" v-if="gp.is_host"> {{gp.player.user_name}} </td>
                                            <td v-for="gp in game.gamePlayers" v-if="gp.is_host == false"> {{gp.player.user_name}} </td>
                                            <td v-for="gp in game.gamePlayers" v-if="gp.is_host == false"> {{gp.join_time}} </td>
                                        </tr>
                                    </tbody>
                                </table>

                            </div>
                        </div>
                        `
        },
        player_boardscore_page: {
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
                                        <tr v-for="player in players">
                                            <th scope="row">{{game.game_id}}</th>
                                            <td>{{game.game_name}}</td>
                                            <td v-for="gp in game.gamePlayers" v-if="gp.is_host"> {{gp.join_time}} </td>
                                            <td v-for="gp in game.gamePlayers" v-if="gp.is_host"> {{gp.player.user_name}} </td>
                                            <td v-for="gp in game.gamePlayers" v-if="gp.is_host == false"> {{gp.player.user_name}} </td>
                                            <td v-for="gp in game.gamePlayers" v-if="gp.is_host == false"> {{gp.join_time}} </td>
                                        </tr>
                                    </tbody>
                                </table>

                            </div>
                        </div>
                        `
        },

    }
})