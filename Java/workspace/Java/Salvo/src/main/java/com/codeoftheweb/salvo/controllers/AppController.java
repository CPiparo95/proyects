package com.codeoftheweb.salvo.controllers;
import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repositorys.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.*;

@RestController
@RequestMapping("/api")
public class AppController {

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

    @RequestMapping("/players")
    public List<Map<String, Object>> getPlayers(){
        return playerRepo.findAll().stream().map(Player::playerWithGamesDTO).collect(Collectors.toList());
    }

    @RequestMapping("/games")
    public Map<String, Object> getGames(Authentication authentication){
        Map<String, Object> dto = new LinkedHashMap<>();

        if(isGuest(authentication))
            dto.put("player", "guest");
        else
            dto.put("player", playerRepo.findByUsername(authentication.getName()).playerDTO());

        dto.put("games", gameRepo.findAll().stream().map(Game::gameWithPlayersDTO).collect(Collectors.toList()));
        return dto;
    }

    @RequestMapping("/game_view/{gamePlayerID}")
    public Map<String, Object> getGameView(@PathVariable long gamePlayerID){
        return this.gameViewDTO(gamePlayerRepo.findById(gamePlayerID).orElse(null));
    }

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
