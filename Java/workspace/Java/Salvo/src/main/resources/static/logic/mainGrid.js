const app = new Vue({
    el: '#app',
    data: {
        shipsPositioned: false,
        salvoesFired: false,
        salvoesPositionsNotFire: [],
        salvoesPositionsFire: [],
        turno : 0
    },
    methods:{
        logOut: function(ev){
            return fetch("/api/logout",{
                method: "Post"
            })
            .then(response =>{
                 if(response.status==200) {
                    alert("Se a desconectado correctamente")
                    window.location.replace('index.html')
                    }
                })
            }
    }
})