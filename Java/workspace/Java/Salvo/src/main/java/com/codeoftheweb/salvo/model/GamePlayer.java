package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.*;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER, cascade= CascadeType.ALL)
    private Set<Ship> ship = new HashSet<>();

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER, cascade= CascadeType.ALL)
    private Set<Salvoes> salvoes = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    private LocalDateTime joinTime;

    public GamePlayer(){}

    public GamePlayer(Player player, Game game, LocalDateTime joinTime){
        this.player = player;
        this.game = game;
        this.joinTime = joinTime;
    }

    public Map<String, Object> gameDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("game_player_id",this.id);
        dto.put("join_time", this.getJoinTime());
        dto.put("game", this.getGame().gameDTO());
        return dto;
    }

    public Map<String, Object> playerDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("game_player_id",this.id);
        dto.put("join_time", this.getJoinTime());
        dto.put("player", this.getPlayer().playerDTO());
        return dto;
    }

    public Game getGame(){
        return this.game;
    }

    public Set<Ship> getShip() {
        return ship;
    }

    public Player getPlayer(){
        return this.player;
    }

    public void addShip(Ship ship){
        this.ship.add(ship);
        ship.setGamePlayer(this);
    }

    public Set<Salvoes> getSalvoes() {
        return salvoes;
    }

    public void addSalvoes(Salvoes salvoes){
        this.salvoes.add(salvoes);
        salvoes.setGamePlayer(this);
    }

    public LocalDateTime getJoinTime(){
        return this.joinTime;
    }
}
