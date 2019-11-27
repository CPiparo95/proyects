package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

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
    @Column(name = "locations")
    private List<String> locations = new ArrayList<>();

    private Integer turn;

    public Salvoes() { }

    public Salvoes(Integer turn, List<String> locations) {
        this.turn = turn;
        this.locations = locations;
    }

    public Salvoes(List<String> locations) {
        this.locations = locations;
    }

    public Map<String, Object> salvoesDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("salvo_id",this.id);
        dto.put("player_username", this.getGamePlayer().getPlayer().getUserName());
        dto.put("turn", this.getTurn());
        dto.put("fire_positions",this.getLocations());
        dto.put("hits", this.getHits());
        return dto;
    }


    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public Integer getTurn() {
        return turn;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    //AGREGA LOS HITS A SALVO
    public List<String> getHits(){
        List<String> allCells = new ArrayList<>();
        getGamePlayer().getOponentGP(getGamePlayer()).getShip().forEach(ship -> allCells.addAll(ship.getLocations()));

        List<String> allSalvoCells = new ArrayList<>();

        for (String allCell : allCells) {
            for (int j = 0; j < getLocations().size(); j++) {
                if (allCell.equals(getLocations().get(j))) {
                    allSalvoCells.add(allCell);
                }
            }
        }
        return allSalvoCells;
    }

}
