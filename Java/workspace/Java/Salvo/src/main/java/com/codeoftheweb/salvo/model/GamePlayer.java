package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @ElementCollection
    @Column(name = "sinks")
    private Set<Ship> sinks;

    private LocalDateTime joinTime;

    private String state;

    private Boolean host;

    public GamePlayer(){}

    public GamePlayer(Player player, Game game, LocalDateTime joinTime, Boolean host){
        this.host = host;
        this.player = player;
        this.game = game;
        this.joinTime = joinTime;
    }

    public GamePlayer(Player player, Game game, LocalDateTime joinTime, Boolean host, String state){
        this.host = host;
        this.player = player;
        this.game = game;
        this.joinTime = joinTime;
        this.state = state;
    }

    public Map<String, Object> gameDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("game_player_id",this.id);
        dto.put("join_time", this.convertToNormalTime(getJoinTime()));
        dto.put("is_host",this.getHost());
        dto.put("game", this.getGame().gameDTO());
        dto.put("state", this.getState());

        Score score = this.getGame().getScoreByPlayer(this.getPlayer());
        if (score != null){
            dto.put("score", score.getScore());
        }else
            dto.put("score", null);

        return dto;
    }

    public Map<String, Object> playerDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("game_player_id",this.id);
        dto.put("join_time", this.convertToNormalTime(getJoinTime()));
        dto.put("is_host",this.getHost());
        dto.put("state", this.getState());
        dto.put("player", this.getPlayer().playerDTO());
        dto.put("sinks",this.getSinks().stream().map(Ship::shipDTO));

        Score score = this.getPlayer().getScoreByGame(this.getGame());
        if (score != null){
            dto.put("score", score.getScore());
        }else
            dto.put("score", null);

        return dto;
    }

    public Boolean getHost() {
        return host;
    }

    public void setHost(Boolean host) {
        this.host = host;
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

    private String convertToNormalTime (LocalDateTime joinTime){

        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return joinTime.format(formateador);
    }

    public Set<Ship> getSinks() {
        return sinks;
    }

    public void setSinks(Ship sink) {
        this.sinks.add(sink);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
