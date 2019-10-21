const app = new Vue({
  el: '#app',
  mounted() {
    this.dbAsk()
  },
  data: {

    actual_page: "home_tab",
    nextGames: {}

  },
  methods: {
    async dbAsk() {

      //create reference
      const dbRefObject = await firebase.database().ref('futureGames/');

      //Sync Object changes
      const mande = await dbRefObject.on('value', snap => this.nextGames = snap.val());
    }
  },
  components: {
    home_page: {
      template: `
                        <div>
                            <div class="text-center">
                                <img id="imagenFondo" src="images/fondo.jpg" class="rounded">
                            </div>
                        </div>
                        `
    },
    next_game: { // es el template de 1 solo juego generico segun parametros que le lleguen
      props: ['nextgame'],
      methods: {},
      template: `
                        <div class="container">
                          <H4 class="dateTime"> 
                            FECHA: {{ nextgame.Date }}      HORA: {{ nextgame.Time }} </H4>
                          <div class="container2">
                            <div> <img v-bind:src="'images/'+nextgame.Team1 + '.png'" class="teamImage"> </div>
                            <div> <img src="images/vs.jpg" class="teamImage"> </div>
                            <div> <img v-bind:src="'images/'+nextgame.Team2 + '.png'" class="teamImage"> </div>
                          </div>

                          <div id="MapsDiv">
                          <div v-if="nextgame.Location == 'AJ Katzenmaier'">
                          <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2969.654060426158!2d-87.
				6312390851344!3d41.90029637200482!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x880fd
				34e07f6bac3%3A0x68a82e5d59952c86!2s24+W+Walton+St%2C+Chicago%2C+IL+60610%2C+EE.+UU.!5e0!3m2
				!1ses!2sar!4v1565094470215!5m2!1ses!2sar" 
				width="75vw" frameborder="0" style="border:0" allowfullscreen></iframe>
                          </div>

                          <div v-if="nextgame.Location == 'Greenbay'">
                          <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2969.025698674534!2d-87.
				64002798513393!3d41.913806271159!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x880fd3
				4073f306a3%3A0x9e1726bbf8f23f0e!2s1734+N+Orleans+St%2C+Chicago%2C+IL+60614%2C+EE.+UU.!5e0!3
				m2!1ses!2sar!4v1565095102737!5m2!1ses!2sar" 
				width="75vw" frameborder="0" style="border:0" allowfullscreen></iframe>
                          </div>

                          <div v-if="nextgame.Location == 'Howard A Yeager'">
                          <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2968.58549738468!2d-87.6
				6511458513358!3d41.92326857056624!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x880fd
				2e37f9b8d2d%3A0x62ad8b907dd755d6!2s2245+N+Southport+Ave%2C+Chicago%2C+IL+60614%2C+EE.+UU.!5
				e0!3m2!1ses!2sar!4v1565098075861!5m2!1ses!2sar" 
				width="75vw" frameborder="0" style="border:0" allowfullscreen></iframe>
                          </div>

                          <div v-if="nextgame.Location == 'North'">
                          <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2969.3376141595954!2d-8
				7.64837698513415!3d41.90710047157887!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x8
				80fd33af0e6ccc3%3A0x26c81c1d557667da!2s1409+N+Ogden+Ave%2C+Chicago%2C+IL+60610%2C+EE.+UU.!
				5e0!3m2!1ses!2sar!4v1565098671102!5m2!1ses!2sar" 
				width="75vw" frameborder="0" style="border:0" allowfullscreen></iframe>
                          </div>

                          <div v-if="nextgame.Location == 'South'">
                          <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2968.7477648265603!2d-8
				7.65355538513371!3d41.91978077078483!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x8
				80fd3196fb41dc7%3A0x970be7f7d6336df5!2s2101+N+Fremont+St%2C+Chicago%2C+IL+60614%2C+EE.+UU.
				!5e0!3m2!1ses!2sar!4v1565098878903!5m2!1ses!2sar"  
				width="75vw" frameborder="0" style="border:0" allowfullscreen></iframe>
                          </div>

                          <div v-if="nextgame.Location == 'Marjorie P Hart'">
                          <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2968.2917289530847!2d-87
				.64808628513342!3d41.929582270170776!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x88
				0fd30f2630e551%3A0x3e719e44a5cef714!2s2625+N+Orchard+St%2C+Chicago%2C+IL+60614%2C+EE.+UU.!5
				e0!3m2!1ses!2sar!4v1565098551576!5m2!1ses!2sar"
				width="100%" frameborder="0" style="border:0" allowfullscreen></iframe>
                          </div>
                          </div>
                        </div>
                        `
    },
    menu_hamburguer: { // es el template del menu hamburguesa
      props: [],
      methods: {},
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