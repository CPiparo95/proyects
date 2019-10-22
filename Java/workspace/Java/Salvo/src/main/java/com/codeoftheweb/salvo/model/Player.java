package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
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

    public Map<String, Object> PlayerWithGamesDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("user_name", this.getUserName());
        dto.put("games", this.getGames().stream().map(Game::GameDTO));
        //dto.put("join_time", this.getJoinTime());
        return dto;
    }

    public Map<String, Object> PlayerDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id",this.id);
        dto.put("user_name", this.getUserName());
        return dto;
    }

    public Set<Game> getGames(){
        return this.gamePlayers.stream().map(GamePlayer::getGame).collect(Collectors.toSet());
    }

    //public Set<LocalDateTime> getJoinTime() {
    //    return this.gamePlayers.stream().map(GamePlayer::getJoinTime).collect(Collectors.toSet());
    //}

    public String getUserName() {
        return username;
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
