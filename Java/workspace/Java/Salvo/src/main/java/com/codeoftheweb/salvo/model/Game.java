package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private LocalDateTime creationTime;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER, cascade= CascadeType.ALL)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER, cascade= CascadeType.ALL)
    private Set<Score> scores = new HashSet<>();

    public Game() { }

    public Game(LocalDateTime creationTime) {

        this.creationTime = creationTime;
    }

    public Map<String, Object> gameDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("game_id",this.id);
        dto.put("creation_date", this.convertToNormalTime(getCreationTime()));
        return dto;
    }

    public Map<String, Object> gameWithPlayersDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("game_id",this.id);
        dto.put("creation_date", this.convertToNormalTime(getCreationTime()));
        dto.put("game_players", this.getGamePlayers().stream().map(GamePlayer::playerDTO));
        return dto;
    }

    public Score getScoreByPlayer(Player player){
        return this.scores.stream().filter(score -> score.getPlayer().getId() == player.getId()).findFirst().orElse(null);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public String convertToNormalTime (LocalDateTime joinTime){

        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        String formatDateTime = joinTime.format(formateador);

        return formatDateTime;
    }
}

