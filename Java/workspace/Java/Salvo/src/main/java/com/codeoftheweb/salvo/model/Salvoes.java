package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Entity
public class Salvoes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_player_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    private Set<Positions> positions;

    private String turn;

    public Salvoes() { }

    public Salvoes(String turn, Set<Positions> positions) {
        this.turn = turn;
        this.positions = positions;
    }

    public Map<String, Object> salvoesDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("salvo_id",this.id);
        dto.put("player_username", this.getGamePlayer().getPlayer().getUserName());
        dto.put("turn", this.getTurn());
        dto.put("fire_positions",this.getPositions());
        return dto;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public Set<Positions> getPositions() {
        return positions;
    }

    public void setPositions(Set<Positions> positions) {
        this.positions = positions;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

}
