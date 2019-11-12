const app = new Vue({
    el: '#app',
    data: {
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