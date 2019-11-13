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

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private GameRepository gameRepo;

    private Game game;

    @Autowired
    private GamePlayerRepository gamePlayerRepo;

    @Autowired
    private SalvoesRepository salvoesRepo;

    //GET PIDE PLAYERS
    @RequestMapping("/players")
    public List<Map<String, Object>> getPlayers() {
        return playerRepo.findAll().stream().map(Player::playerWithGamesDTO).collect(Collectors.toList());
    }

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
        map.put("error", "mensaje de prueba, soy un user creado");
        return new ResponseEntity<>(map, HttpStatus.CREATED);

    }

    //POST PARA CREAR JUEGOS
    @RequestMapping(value = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(
            @RequestParam String userName) {

        Map<String, Object> map = new HashMap<>();

        if (userName.isEmpty()) {
            map.put("error", "te haces el pistola tocandome el back?");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }

        if (playerRepo.findByUsername(userName) != null) {
            map.put("error", "el usuario que entro no existe, terrible bug, probaste apagando y volviendo a encender?");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }


        Game game = gameRepo.save(new Game(LocalDateTime.now()));
        map.put("Exito", "El juego ha sido creado");
        return new ResponseEntity<>(map,HttpStatus.CREATED);

        //gamePlayerRepo.save(new GamePlayer())
        //return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //GET PIDE JUEGOS, TAMBIEN INDICA QUIEN ESTA CONECTADO
    @RequestMapping("/games")
    public Map<String, Object> getGames(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();

        if (isGuest(authentication))
            dto.put("player", "guest");
        else
            dto.put("player", playerRepo.findByUsername(authentication.getName()).playerDTO());

        dto.put("games", gameRepo.findAll().stream().map(Game::gameWithPlayersDTO).collect(Collectors.toList()));
        return dto;
    }

    //GET QUE INDICA UN JUEGO EN PARTICULAR
    @RequestMapping("/game_view/{gamePlayerID}")
    public Map<String, Object> getGameView(@PathVariable long gamePlayerID){
        return this.gameViewDTO(gamePlayerRepo.findById(gamePlayerID).orElse(null));
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



    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }
}
