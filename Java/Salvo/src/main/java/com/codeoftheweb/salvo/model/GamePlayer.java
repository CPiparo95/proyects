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

    private LocalDateTime joinTime;

    private Boolean host;

    public GamePlayer(){}

    public GamePlayer(Player player, Game game, LocalDateTime joinTime, Boolean host){
        this.host = host;
        this.player = player;
        this.game = game;
        this.joinTime = joinTime;
    }

    public Map<String, Object> gameDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("game_player_id",this.id);
        dto.put("join_time", this.convertToNormalTime(getJoinTime()));
        dto.put("is_host",this.getHost());
        dto.put("game", this.getGame().gameDTO());
        dto.put("state", this.getState());
        dto.put("sinks",this.getSinks(this).stream().map(Ship::shipDTO));

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
        dto.put("sinks",this.getSinks(this).stream().map(Ship::shipDTO));

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getState() {
        if (this.getShip().size() == 0) {
            return "creacion de barcos";
        }else if (getOponentGP(this) == null){
            return "Espera";
        }else if (this.getSinks(this).size() == 5 && this.getSinks(getOponentGP(this)).size() == 5){
            return "Empataste, verguenza.";
        }else if (this.getSinks(this).size() == 5 && this.getSinks(getOponentGP(this)).size() != 5){
            return "Ganaste";
        }else if (this.getSinks(this).size() != 5 && this.getSinks(getOponentGP(this)).size() == 5) {
            return "Perdiste, Verguenza.";
        }else if (this.getShip().size() == 5 && getOponentGP(this).getShip().size() == 0) {
            return "Espera";
        }else if (this.getSalvoes().size() > getOponentGP(this).getSalvoes().size()) {
            return "Espera";
        }else if (this.getHost() && this.getSalvoes().size() == getOponentGP(this).getSalvoes().size()){
            return "Envio de salvos";
        }else if (!this.getHost() && this.getSalvoes().size() == getOponentGP(this).getSalvoes().size()){
            return "Espera";
        }else{
            return "Envio de salvos";
        }
    }

    public GamePlayer getOponentGP (GamePlayer myGP){
        return myGP.getGame().getGamePlayers().stream().filter
                (gpa -> gpa.getId() != myGP.getId()).findFirst().orElse(null);
    }

    //AGREGA LOS SINKS A SALVO
    public Set<Ship> getSinks(GamePlayer gpNuestro){
        Set <Ship> sinks = new HashSet<>();

        GamePlayer gpEnemigo = getOponentGP(gpNuestro);
        Set<Salvoes> salvos = gpNuestro.getSalvoes();

        if (gpEnemigo == null) {
            return sinks;
        }else {
            List<String> allCells = new ArrayList<>();
            for (Salvoes salvo : salvos) {
                allCells.addAll(salvo.getHits());
            }

            for (Ship ship : gpEnemigo.getShip()) {
                if (allCells.containsAll(ship.getLocations())) {
                    sinks.add(ship);
                }
            }
        }
        return sinks;
    }



}
