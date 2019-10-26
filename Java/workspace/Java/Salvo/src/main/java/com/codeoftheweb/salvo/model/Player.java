package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String username;
    private String Password;


    @OneToMany(mappedBy="player", fetch=FetchType.EAGER, cascade= CascadeType.ALL)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    public Player() { }

    public Player(String userMail, String password) {
        this.username = userMail;
        this.Password = password;
    }

    public Map<String, Object> playerWithGamesDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("player_id",this.id);
        dto.put("user_name", this.getUserName());
        dto.put("game_player", this.getGamePlayers().stream().map(GamePlayer::gameDTO));
        return dto;
    }

    public Map<String, Object> playerDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("player_id",this.id);
        dto.put("user_name", this.getUserName());
        return dto;
    }

    public String getUserName() {
        return username;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public void setUserName(String userMail) {
        this.username = userMail;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }
}
