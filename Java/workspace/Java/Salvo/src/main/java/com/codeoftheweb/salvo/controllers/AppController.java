package com.codeoftheweb.salvo.controllers;
import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repositorys.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.*;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private ShipRepository shipRepo;

    private GamePlayer gp;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private GamePlayerRepository gamePlayerRepo;

    @Autowired
    private SalvoesRepository salvoesRepo;

    //POST PARA CREAR PLAYERS
    @RequestMapping(value = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> register(
            @RequestParam String email,
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
    public ResponseEntity<Map<String, Object>> joinGame(Authentication authentication, @PathVariable Long gameId) {
        Map<String, Object> dto = new HashMap<>();
        if(isGuest(authentication)) {
            dto.put("ERROR", "No se puede loguear si estas desconectado, por favor conectate");
            return new ResponseEntity<>(dto,HttpStatus.UNAUTHORIZED);
        }

        Game game = gameRepo.findById(gameId).orElse(null);
        if(game == null){
            dto.put("ERROR", "No se puede conectar a un juego inexistente");
            return new ResponseEntity<>(dto,HttpStatus.NOT_FOUND);
        }else if (game.getGamePlayers().size() > 1){
            dto.put("ERROR", "La sala esta llena");
            return new ResponseEntity<>(dto,HttpStatus.FORBIDDEN);
        }else{
            Player player = playerRepo.findByUsername(authentication.getName());
            if (game.getGamePlayers().stream().anyMatch(gp -> gp.getPlayer().getId() == player.getId())){
                dto.put("ERROR", "No podes jugar contra vos mismo...");
                return new ResponseEntity<>(dto,HttpStatus.FORBIDDEN);
            }else{
                GamePlayer newgp = new GamePlayer(player,game,LocalDateTime.now(),false);
                dto.put("Success", "Te has unido al juego correctamente");
                return new ResponseEntity<>(dto,HttpStatus.CREATED);
            }
        }
    }

    //POST PARA CREAR JUEGOS
    @RequestMapping(value = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {

        System.out.println(authentication.getAuthorities() + "get Authorities");
        System.out.println(authentication.getCredentials() + "get Credentials");
        System.out.println(authentication.getDetails() + "get details");
        System.out.println(authentication.getPrincipal() + "get principal");
        System.out.println(authentication.getName() + "get name");
        System.out.println(authentication.getClass() + "get class");
        Map<String, Object> dto = new HashMap<>();

        if (isGuest(authentication)) {
            dto.put("Error", "Guest cannot create games");
            return new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
        }

        Player player = playerRepo.findByUsername(authentication.getName());
        Game game = gameRepo.save(new Game(LocalDateTime.now()));

        gp = gamePlayerRepo.save(new GamePlayer(player,game,game.getCreationTime(),true));
        dto.put(" Success ", " El juego ha sido creado, y usted se ha unido a el");
        return new ResponseEntity<>(dto,HttpStatus.CREATED);
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
    public Map<String, Object> getGameView(@PathVariable long gamePlayerID){
        return this.gameViewDTO(gamePlayerRepo.findById(gamePlayerID).orElse(null));
    }

    //GET QUE INDICA UN JUEGO EN PARTICULAR
    /* @RequestMapping("/game_view/{gamePlayerID}")
    public ResponseEntity<Map<String, Object>> getGameView(@PathVariable long gamePlayerID, Authentication auth){

        ResponseEntity<Map<String, Object>> response;
        Map<String, Object> dto = new HashMap<>();
        //NO ENTIENDO PORQUE DA ERROR ACA, SIMPLEMENTE NO ME LO EXPLICO
        GamePlayer gp = gamePlayerRepo.findById(gamePlayerID);

        if (auth.getName() != gp.getPlayer().getUserName()) {
            dto.put("Error", "cheat is not allowed!");
            return response = new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
        }

        dto.put("data",this.gameViewDTO(gamePlayerRepo.findById(gamePlayerID).orElse(null)));
        return response = new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
    }
*/
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
            dto.put("Ships", gamePlayer.getShip().stream().map(Ship::shipDTO));
            dto.put("Salvoes", gamePlayer.getGame().getGamePlayers().stream().flatMap(gp -> gp.getSalvoes().stream()
                    .map(Salvoes::salvoesDTO)));
        }else
        { dto.put("ERROR", "no such game"); }

            return dto;
    }

    //Authentication Method
    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }
}
