package com.codeoftheweb.salvo.controllers;
import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repositorys.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

@RestController
@RequestMapping("/api")
public class AppController {

    private GamePlayer gp;

    private Ship ship;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private ShipRepository shipRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private GamePlayerRepository gamePlayerRepo;

    @Autowired
    private SalvoesRepository salvoesRepo;

    @Autowired
    private ScoreRepository scoreRepo;

    //POST PARA CREAR PLAYERS
    @RequestMapping(value = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> register(@RequestParam String email,
                                                        @RequestParam String username,
                                                        @RequestParam String password) {

        Map<String, Object> map = new HashMap<>();

        if (email.isEmpty() || password.isEmpty()) {
            map.put("error", "no ves que hay campos vacios flaco?");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }

        if (playerRepo.findByUsername(username) != null) {
            map.put("error", "el usuario que elegiste ya existe, probaste apagando y volviendo a encender?");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }

        playerRepo.save(new Player(username, encoder.encode(password), email));
        map.put("Success!", "has creado al usuario, pero aun no esta logueado");
        return new ResponseEntity<>(map, HttpStatus.CREATED);

    }

    //POST PARA UNIRSE A JUEGOS
    @RequestMapping(value = "/joinGame/{gameId}", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(Authentication authentication,
                                                        @PathVariable Long gameId) {
        Map<String, Object> dto = new HashMap<>();
        if (isGuest(authentication)) {
            dto.put("ERROR", "No se puede loguear si estas desconectado, por favor conectate");
            return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
        }

        Game game = gameRepo.findById(gameId).orElse(null);
        if (game == null) {
            dto.put("ERROR", "No se puede conectar a un juego inexistente");
            return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
        }else if (game.getGamePlayers().stream().anyMatch(gp -> gp.getState().equals("Ganaste") || gp.getState().equals("Perdiste") || gp.getState().equals("Empataste, Verguenza."))) {
            dto.put("ERROR", "El juego termino, no se puede unir a el.");
            return new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
        }else if (game.getGamePlayers().size() > 1) {
            dto.put("ERROR", "La sala esta llena");
            return new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
        } else {
            Player player = playerRepo.findByUsername(authentication.getName());
            if (game.getGamePlayers().stream().anyMatch(gp -> gp.getPlayer().getId() == player.getId())) {
                dto.put("ERROR", "No podes jugar contra vos mismo...");
                return new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
            } else {
                GamePlayer newgp = new GamePlayer(player, game, LocalDateTime.now(), false);
                gamePlayerRepo.save(newgp);
                dto.put("Success", "Te has unido al juego correctamente");
                return new ResponseEntity<>(dto, HttpStatus.CREATED);
            }
        }
    }

    //POST PARA CREAR JUEGOS
    @RequestMapping(value = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {

        Map<String, Object> dto = new HashMap<>();

        if (isGuest(authentication)) {
            dto.put("Error", "Guest cannot create games");
            return new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
        }

        Player player = playerRepo.findByUsername(authentication.getName());
        Game game = gameRepo.save(new Game(LocalDateTime.now()));

        GamePlayer gp = gamePlayerRepo.save(new GamePlayer(player, game, game.getCreationTime(),
                true));
        dto.put(" Success ", " El juego ha sido creado, y usted se ha unido a el");
        dto.put("gamePlayerID", gp.getId());
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //POST PARA GUARDAR LOS BARCOS
    @RequestMapping(value = "/placeShips/{gpId}", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addShips(Authentication authentication,
                                                        @PathVariable Long gpId,
                                                        @RequestBody Set<Ship> ships) {

        Map<String, Object> dto = new HashMap<>();

        if (isGuest(authentication)) {
            dto.put("Error", "Guest's cannot play games or place ships");
            return new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
        } else {
            GamePlayer gp = gamePlayerRepo.findById(gpId).orElse(null);
            Player player = playerRepo.findByUsername(authentication.getName());
            if (gp == null) {
                dto.put("Error", "This game does not exist!");
                return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
            }else if (gp.getState().equals("Ganaste") || gp.getState().equals("Perdiste") ||
                    gp.getState().equals("Empataste, verguenza.")) { //STATE
                dto.put("Error", "The game is finished, you cannot place ships in this game.");
                return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
            }else if (!gp.getState().equals("creacion de barcos")) {
                dto.put("Error", "you cannot create ships!");
                return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
            }else if (gp.getPlayer().getId() != player.getId()) {
                dto.put("Error", "You are not playing this game!");
                return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
            } else if (gp.getShip().size() > 0) {
                dto.put("Error", "there are already ships positioned");
                return new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
            } else if (ships == null || ships.size() != 5) {
                dto.put("Error", "There is not enough ships or you placed yoo much ships! (You have to add 5 ships)");
                return new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
            } else {
                if (ships.stream().anyMatch(this::isOutOfRange)) {
                    dto.put("Error", "You have ships out of range");
                    return new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
                } else if (ships.stream().anyMatch(this::isNotConsecutive)) {
                    dto.put("Error", "Your ships are not consecutive!");
                    return new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
                } else if (this.areOverlapped(ships)) {
                    dto.put("Error", "Your ships are overlapped!");
                    return new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
                } else {
                    ships.forEach(gp::addShip);

                    gamePlayerRepo.save(gp);

                    dto.put("Success", "Ships have been added");
                    return new ResponseEntity<>(dto, HttpStatus.CREATED);
                }
            }
        }
    }

    //POST PARA GUARDAR SALVOS
    @RequestMapping(value = "/fireSalvoes/{gpId}", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addSalvoes(Authentication authentication,
                                                          @PathVariable Long gpId,
                                                          @RequestBody Salvoes salvoes) {
        Map<String, Object> dto = new HashMap<>();
        if (isGuest(authentication)) {
            dto.put("Error", "Guest's cannot play games or fire salvoes");
            return new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
        } else {
            GamePlayer gp = gamePlayerRepo.findById(gpId).orElse(null);
            Player player = playerRepo.findByUsername(authentication.getName());

            //SETEA EL TURNO DEL SALVO QUE ACABA DE LLEGAR
            salvoes.setTurn(gp.getSalvoes().size()+1);
            if (gp == null) {
                dto.put("Error", "This game does not exist!");
                return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
            }else if (gp.getState().equals("Ganaste") || gp.getState().equals("Perdiste") || gp.getState().equals("Empataste, verguenza.")) { //STATE
                dto.put("Error", "The game is finished, you cannot send salvoes in this game.");
                return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
            }else if (gp.getPlayer().getId() != player.getId()) {
                dto.put("Error", "You are not playing this game!");
                return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
            } else if (gp.getSalvoes().stream().anyMatch(salvo -> salvo.getTurn() == salvoes.getTurn())) {
                dto.put("Error", "the salvoes have been already fired!");
                return new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
            } else if (salvoes == null || salvoes.getLocations().size() != 5) {
                dto.put("Error", "There is not enough salvoes fired or you have fired more than 5 salvoes");
                return new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
            }else if (this.areOverlappedsalvos(gp.getSalvoes(), salvoes)) {
                dto.put("Error", "No seas boludo, tenes salvos overlapeados");
                return new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
            }else if (getOponentGP(gp) == null) {
                dto.put("Error", "You cannot fire salvoes if you have no opponent! what you want to fire? water?");
                return new ResponseEntity<>(dto, HttpStatus.I_AM_A_TEAPOT);
            }else if (getOponentGP(gp).getShip().size() == 0) {
                dto.put("Error", "You cannot fire salvos if the opponent has not placed it's ships!");
                return new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
            }else if (!gp.getState().equals("Envio de salvos")){
                dto.put("Error", "you have to wait to you opponent to fire salvos!");
                return new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
            }else{
            gp.addSalvoes(salvoes);//AGREGA LOS SALVOS A LA RELACION
            GamePlayer contraryGp = getOponentGP(gp);

                gamePlayerRepo.save(gp);
                gamePlayerRepo.save(contraryGp);
            if (isEnd(gp,contraryGp)){
                dto.put("End", "The game is finished!" + gp.getState());
                return new ResponseEntity<>(dto, HttpStatus.I_AM_A_TEAPOT);
            }else {
                dto.put("Success", "Salvoes have been fired!");
                return new ResponseEntity<>(dto, HttpStatus.CREATED);
            }
        }
    }
}

    //GET PIDE JUEGOS, TAMBIEN INDICA QUIEN ESTA CONECTADO
    @RequestMapping("/games")
    public Map<String, Object> getGames() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("games", gameRepo.findAll().stream().map(Game::gameWithPlayersDTO).collect(Collectors.toList()));
        return dto;
    }

    //GET PIDE PLAYERS
    @RequestMapping("/players")
    public List<Map<String, Object>> getPlayers() {
        return playerRepo.findAll().stream().map(Player::playerWithGamesDTO).collect(Collectors.toList());
    }

    //GET DONDE INDICA QUIEN ESTA CONECTADO
    @RequestMapping("/connected")
    public Map<String, Object> getUserConnected(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();

        if (isGuest(authentication))
            dto.put("player", "guest");
        else
            dto.put("player", playerRepo.findByUsername(authentication.getName()).playerDTO());
        return dto;
    }

    //GET QUE INDICA UN JUEGO EN PARTICULAR
     @RequestMapping("/game_view/{gamePlayerID}")
    public ResponseEntity<Map<String, Object>> getGameView(@PathVariable long gamePlayerID, Authentication auth){

        ResponseEntity<Map<String, Object>> response;
        Map<String, Object> dto = new HashMap<>();
        GamePlayer gp = gamePlayerRepo.findById(gamePlayerID).orElse(null);

        if (auth.getName() != gp.getPlayer().getUserName()) {
            dto.put("Error", "cheat is not allowed!");
            return response = new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
        }

        dto.put("data",this.gameViewDTO(gamePlayerRepo.findById(gamePlayerID).orElse(null)));
        return response = new ResponseEntity<>(dto, HttpStatus.OK);
    }

    //GET QUE INDICA LOS JUEGOS DE UN JUGADOR EN PARTICULAR
    @RequestMapping("/players/{playerID}")
    public Map<String, Object> getPlayerView(@PathVariable long playerID){
        return this.playerViewDTO(playerRepo.findById(playerID).orElse(null));
    }

    //DTO
    private Map<String, Object> playerViewDTO (Player player){
        Map<String, Object> dto = new LinkedHashMap<>();
        if (player != null) {
            dto = player.playerWithGamesDTO();
        }else
        { dto.put("ERROR", "no such player"); }

        return dto;
    }

    //DTO
    private Map<String, Object> gameViewDTO (GamePlayer gamePlayer){
        Map<String, Object> dto = new LinkedHashMap<>();
        if (gamePlayer != null) {
            dto = gamePlayer.getGame().gameWithPlayersDTO();
            dto.put("ships", gamePlayer.getShip().stream().map(Ship::shipDTO));
            dto.put("salvoes", gamePlayer.getGame().getGamePlayers().stream().flatMap(gp -> gp.getSalvoes().stream()
                    .map(Salvoes::salvoesDTO)));
        }else
        { dto.put("ERROR", "no such game"); }

            return dto;
    }

    //Authentication Method
    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private boolean isEnd(GamePlayer myGp, GamePlayer contraryGp){
        if(myGp.getSalvoes().size() == contraryGp.getSalvoes().size()){
            if (myGp.getSinks(myGp).size() == 5 && contraryGp.getSinks(contraryGp).size() != 5){
                scoreRepo.save(new Score(myGp.getPlayer(),myGp.getGame(),LocalDateTime.now(), 1.0));
                scoreRepo.save(new Score(contraryGp.getPlayer(),myGp.getGame(),LocalDateTime.now(), 0.0));
                return true;
            }else if (myGp.getSinks(myGp).size() == 5 && contraryGp.getSinks(contraryGp).size() == 5){
                scoreRepo.save(new Score(myGp.getPlayer(),myGp.getGame(),LocalDateTime.now(), 0.5));
                scoreRepo.save(new Score(contraryGp.getPlayer(),myGp.getGame(),LocalDateTime.now(), 0.5));
                return true;
            }
            else if (myGp.getSinks(myGp).size() != 5 && contraryGp.getSinks(contraryGp).size() == 5){
                scoreRepo.save(new Score(myGp.getPlayer(),myGp.getGame(),LocalDateTime.now(), 0.0));
                scoreRepo.save(new Score(contraryGp.getPlayer(),myGp.getGame(),LocalDateTime.now(), 1.0));
                return true;

            }else{
                return false;
            }
        }else{
         return false;
        }
    }

    //Metodos especificos para las funciones
    private boolean isOutOfRange(Ship ship){
        for (String cell : ship.getLocations()){
            if(!(cell instanceof String) || cell.length() < 2){
                return true;
            }
            char y = cell.substring(0,1).charAt(0);
            Integer x;
            try{
                x = Integer.parseInt(cell.substring(1));
            }catch (NumberFormatException e){
                x=99;
            };

            if (x < 1 || x > 10 || y < 'A' || y > 'J'){
                return true;
            }
        }
        return false;
    }

    private boolean isNotConsecutive(Ship ship){
        List<String> cells = ship.getLocations();
        boolean isVertical = cells.get(0).charAt(0) != cells.get(1).charAt(0);
        for (int i = 0; i < cells.size(); i++){
            if(i < cells.size() - 1){
                if(isVertical){
                    char yChar = cells.get(i).substring(0,1).charAt(0);
                    char compareChar = cells.get(i+1).substring(0,1).charAt(0);
                    if (compareChar - yChar != 1){
                        return true;
                    }
                } else{
                    Integer xInt = Integer.parseInt(cells.get(i).substring(1));
                    Integer compareInt = Integer.parseInt(cells.get(i+1).substring(1));
                    if (compareInt - xInt != 1){
                        return true;
                    }
                }
            }

            for (int j= i +1; j < cells.size(); j++){
                if (isVertical){
                    if (!cells.get(i).substring(1).equals(cells.get(j).substring(1))){
                        return true;
                    }
                }else{
                    if (!cells.get(i).substring(0,1).equals(cells.get(j).substring(0,1))){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean areOverlapped(Set<Ship> ships){
        List<String> allCells = new ArrayList<>();
        ships.forEach(ship -> allCells.addAll(ship.getLocations()));
        for (int i = 0; i < allCells.size(); i ++){
            for (int j=i+1; j< allCells.size(); j++ ){
                if (allCells.get(i).equals(allCells.get(j))){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean areOverlappedsalvos(Set<Salvoes> salvoes, Salvoes intendingSalvo){
        List<String> allCells = new ArrayList<>();
        salvoes.forEach(salvo -> allCells.addAll(salvo.getLocations()));
        allCells.addAll(intendingSalvo.getLocations());
        for (int i = 0; i < allCells.size(); i ++){
            for (int j=i+1; j< allCells.size(); j++ ){
                if (allCells.get(i).equals(allCells.get(j))){
                    return true;
                }
            }
        }
        return false;
    }

    private GamePlayer getOponentGP (GamePlayer myGP){
        return myGP.getGame().getGamePlayers().stream().filter
                (gpa -> gpa.getId() != myGP.getId()).findFirst().orElse(null);
    }
}



