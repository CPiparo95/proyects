package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String gameName;
    private LocalDateTime creationDate;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER, cascade= CascadeType.ALL)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    public Game() { }

    public Game(String gameName, LocalDateTime creationDate) {

        this.gameName = gameName;
        this.creationDate = creationDate;
    }

    public Map<String, Object> GameDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id",this.id);
        dto.put("game_name", this.getGameName());
        dto.put("creation_date", this.getCreationDate());
        return dto;
    }

    public Map<String, Object> GameWithPlayersDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id",this.id);
        dto.put("game_name", this.getGameName());
        dto.put("creation_date", this.getCreationDate());
        dto.put("players", this.GetPlayer().stream().map(Player::PlayerDTO));
        return dto;
    }

    public Set<Player> GetPlayer(){
        return this.gamePlayers.stream().map(GamePlayer::getPlayer).collect(Collectors.toSet());
    }


    public String getGameName() {
        return gameName;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}