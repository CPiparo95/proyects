package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private ShipType shipType;

    @ElementCollection
    private Set<ShipPositions> shipPositions;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_player_id")
    private GamePlayer gamePlayer;


    public Ship() { }

    public Ship(ShipType shiptype, Set<ShipPositions> shipPositions) {
        this.shipType = shiptype;
        this.shipPositions = shipPositions;
    }

    public Map<String, Object> shipDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id",this.id);
        dto.put("ship_type", this.getShipType());
        dto.put("ship_positions",this.getShipPositions());
        return dto;
    }

    public ShipType getShipType() {
        return this.shipType;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public Set<ShipPositions> getShipPositions() {
        return shipPositions;
    }
}
