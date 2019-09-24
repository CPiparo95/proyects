const app = new Vue({
    el: '#app',
    mounted(){
      this.dbAsk()
    },
    data: {
        
            actual_page : "home_tab",
            nextGames : {}
        
    },
    methods: {
         async dbAsk(){
            
            //create reference
            const dbRefObject = await firebase.database().ref('futureGames/');

            //Sync Object changes
            const mande = await dbRefObject.on ('value', snap => this.nextGames = snap.val());
        }
    },
    components: {
        home_page: {
            props: ['nextgames'],
            template:   `
                        <div>
                            <div class="text-center">
                                <img id="imagenFondo" src="images/fondo.jpg" class="rounded">
                            </div>
                        <p> {{nextgames}} </p>
                        </div>
                        `
        },
        next_game: { // es el template de 1 solo juego generico segun parametros que le lleguen
            props: ['nextgame'],
            methods: { 
            },
            template:   `
                        <div>
                                <small class="text-muted"> 
                                FECHA: {{ nextgame.Date }} HORA: {{ nextgame.Time }} </small>
                                <br> <br>
                                <div> <img src='{{nextgame}}' > </div>
                                <div> <img src="images/vs.jpg"> </div>
                                <div> <img src=""> </div>
                                
                        </div>
                        `
        },
        menu_hamburguer: { // es el template del menu hamburguesa
            props: [],
            methods: { 
            },
            template: `
            <!--Navbar-->
            <nav class="navbar">
            
              <!-- Collapse button -->
              <button class="navbar-toggler third-button" type="button" data-toggle="collapse" data-target="#navbarSupportedContent22"
                aria-controls="navbarSupportedContent22" aria-expanded="false" aria-label="Toggle navigation">
                <div class="animated-icon3"><span></span><span></span><span></span></div>
              </button>
            
              <!-- Collapsible content -->
              <div class="collapse navbar-collapse" id="navbarSupportedContent22">
            
                <!-- Links -->
                <ul class="navbar-nav mr-auto">
                  <li class="nav-item active">
                    <a class="nav-link" href="index.html"> Home Page <span class="sr-only">(current)</span></a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="#"> Puntuation Page </a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="#"> my Team </a>
                  </li>

                  <li class="nav-item">
                    <a class="nav-link" href="#"> Configuration </a>
                  </li>

                </ul>
                <!-- Links -->
            
              </div>
              <!-- Collapsible content -->
            
            </nav>xtgames="nextGames">
            <!--/.Navbar-->
                        `
        },
    }
})