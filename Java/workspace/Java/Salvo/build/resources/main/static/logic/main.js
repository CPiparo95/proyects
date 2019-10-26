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
                app.list_games = json

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
                                            <th scope="col">Creation date</th>
                                            <th scope="col">User 1 (soon user creator)</th>
                                            <th scope="col">User 2 (soon user guest)</th>
                                            <th scope="col">Join Time (soon)</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr v-for="game in games">
                                            <th scope="row">{{game.id}}</th>
                                            <td>{{game.game_name}}</td>
                                            <td>{{game.creation_date}}</td>
                                            <td>{{game.players[0].user_name}}</td>
                                            <td>{{game.players[1].user_name}}</td>
                                            <td>(soon)</td>
                                        </tr>
                                    </tbody>
                                </table>

                            </div>
                        </div>
                        `
        },

    }
})