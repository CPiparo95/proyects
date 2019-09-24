$(function () {

    $("#openSwipe").swipe({
        swipe: function (event, direction, distance, duration, fingerCount, fingerData) {
            if (direction == "right") {
                $("#menuSwipe").css({
                    "left": "0",
                    "opacity": "1"
                });
            }
        },
        threshold: 0
    });

    $("#closeSwipe").swipe({
        swipe: function (event, direction, distance, duration, fingerCount, fingerData) {
            if (direction != "right") {
                $("#menuSwipe").css({
                    "left": "-100%",
                    "opacity": "0"
                });
            }
        },
        threshold: 0
    });

    $(".fa-bars").click(function () {
        $("#menuSwipe").css({
            "left": "0",
            "opacity": "1"
        });
    });

    $(".fa-arrow-left").click(function () {
        $("section").css({
            "opacity": "0"
        });
        setTimeout(function () {
            history.back();
        }, 255);
    });

    $("#menuSwipe button").click(function () {
        $("#menuSwipe").css({
            "left": "-100%",
            "opacity": "0"
        });
    });

    $("#closeSwipe i").click(function () {
        $("#menuSwipe").css({
            "left": "-100%",
            "opacity": "0"
        });
    });

    $("#btnChangeProfile").click(function () {
        changePageSimple("list-productores.html");
    });

    $("#btnFloatOption").click(function () {
        $("#floatOption").css({
            "right": "0",
            "opacity": "1"
        });
    });

    $("#floatOption #headerOption i").click(function () {
        $("#floatOption").css({
            "right": "-100%",
            "opacity": "0"
        });
    });

    $("#logoHeader").click(function () {
        changePageSimple("main.html");
    });

    jQuery.scrollSpeed(100, 800);

});




window.onload = function () {

    // Variables
    let IMAGENES = [
        '../libs/img/montanya.jpg',
        '../libs/img/parque.jpg',
        '../libs/img/palmeras.jpg'
    ];
    const TIEMPO_INTERVALO_MILESIMAS_SEG = 2000;
    let posicionActual = 0;
    let $botonRetroceder = document.querySelector('#retroceder');
    let $botonAvanzar = document.querySelector('#avanzar');
    let $imagen = document.querySelector('#imagen');
    

    
    chargeOnStart();
    //Empezamos el intervalo
    playIntervalo();

    // Funciones

    /**
     * Funcion que cambia la foto en la siguiente posicion
     */
    function chargeOnStart(){
        for(var i=0; i<IMAGENES.length; i++){
            $imagen.innerHTML = `<img src="${IMAGENES[i]}" style="display:none">`;

        }
    }

    function pasarFoto() {
        if (posicionActual >= IMAGENES.length - 1) {
            posicionActual = 0;
        } else {
            posicionActual++;
        }
        renderizarImagen();
    }

    /**
     * Funcion que cambia la foto en la anterior posicion
     */
    function retrocederFoto() {
        if (posicionActual <= 0) {
            posicionActual = IMAGENES.length - 1;
        } else {
            posicionActual--;
        }
        renderizarImagen();
    }

    /**
     * Funcion que actualiza la imagen de imagen dependiendo de posicionActual
     */
    function renderizarImagen() {
        $imagen.style.backgroundImage = `url(${IMAGENES[posicionActual]})`;
    }

    /** AutoPlay de imagen **/
    function playIntervalo() {
        intervalo = setInterval(pasarFoto, TIEMPO_INTERVALO_MILESIMAS_SEG);
    }

    // Eventos
    $botonAvanzar.addEventListener('click', pasarFoto);
    $botonRetroceder.addEventListener('click', retrocederFoto);

    // Iniciar
    renderizarImagen();
}