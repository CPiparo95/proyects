package com.codeoftheweb.salvo.controllers;
import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repositorys.*;
import org.springframework.beans.factory.annotation.Autowired;
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

   /* @RequestMapping("/ships")
    public List<Map<String, Object>> getPlayers(){
        return playerRepo.findAll().stream().map(Player::PlayerWithGamesDTO).collect(Collectors.toList());
    } */

    @RequestMapping("/players")
    public List<Map<String, Object>> getPlayers(){
        return playerRepo.findAll().stream().map(Player::PlayerWithGamesDTO).collect(Collectors.toList());
    }

    @RequestMapping("/games")
    public List<Map<String, Object>> getGames(){
        return gameRepo.findAll().stream().map(Game::GameWithPlayersDTO).collect(Collectors.toList());
    }


}
