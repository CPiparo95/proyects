package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.codeoftheweb.salvo.repositorys.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SalvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class);
    }

    @Autowired
    PasswordEncoder encoder;

    @Bean
    public CommandLineRunner initData(
            PlayerRepository playerRepository,
            GameRepository gameRepository,
            GamePlayerRepository gamePlayerRepository,
            ShipRepository shipRepository,
            SalvoesRepository salvoesRepository,
            ScoreRepository scoreRepository) {
        return (args) -> {
            // save a couple of players
            Player player1 = playerRepository.save(new Player("Jack", encoder.encode("nosoyhorrible"), "jackpobre@gmail.com"));
            Player player2 = playerRepository.save(new Player("Chloe", encoder.encode("soyUnapassword"),"chloePP@gmail.com"));
            Player player3 = playerRepository.save(new Player("Diana", encoder.encode("soyUnapassword"),"YoNoSoyQuien@gmail.com"));
            Player player4 = playerRepository.save(new Player("Amelia", encoder.encode("soyUnapassword"),"ParaHablarMalDeNadie@gmail.com"));
            Player player5 = playerRepository.save(new Player("Rojelio", encoder.encode("soyUnapassword"),"PeroOsvaldoEraUnIrrespetuoso@gmail.com"));
            Player player6 = playerRepository.save(new Player("Claudio", encoder.encode("Rola"),"Claudiodpiparo@gmail.com"));

            Game juego01 = gameRepository.save(new Game(LocalDateTime.now()));
            Game juego02 = gameRepository.save(new Game(LocalDateTime.now()));
            Game juego03 = gameRepository.save(new Game(LocalDateTime.now()));
            Game juego04 = gameRepository.save(new Game(LocalDateTime.now()));

            String[] barco1 = {"A1","B1","C1","D1"};
            String[] barco2 = {"E8","F8","G8"};
            String[] barco3 = {"D3","D4"};
            String[] barco4 = {"A4","B4","C4","D4","E4"};
            String[] barco5 = {"A7","A8","A9","A10"};
            String[] barco6 = {"C1","D1","E1","F1"};
            String[] barco7 = {"C8","D8","E8"};
            String[] barco8 = {"A3","A4"};
            String[] barco9 = {"B4","C4","D4","E4","F4"};
            String[] barco10 = {"A7","A8","A9","A10"};

            Ship nave01 = shipRepository.save(new Ship("gaucho1", Arrays.asList(barco1)));
            Ship nave02 = shipRepository.save(new Ship("gaucho2",Arrays.asList(barco3)));
            Ship nave03 = shipRepository.save(new Ship("gaucho3",Arrays.asList(barco3)));
            Ship nave04 = shipRepository.save(new Ship("gaucho4",Arrays.asList(barco4)));
            Ship nave05 = shipRepository.save(new Ship("gaucho5",Arrays.asList(barco5)));
            Ship nave06 = shipRepository.save(new Ship("gaucho1",Arrays.asList(barco6)));
            Ship nave07 = shipRepository.save(new Ship("gaucho2",Arrays.asList(barco7)));
            Ship nave08 = shipRepository.save(new Ship("gaucho3",Arrays.asList(barco8)));
            Ship nave09 = shipRepository.save(new Ship("gaucho4",Arrays.asList(barco9)));
            Ship nave10 = shipRepository.save(new Ship("gaucho5",Arrays.asList(barco10)));

            GamePlayer relacion1 = gamePlayerRepository.save(new GamePlayer(player1, juego01, juego01.getCreationTime(), true));
            GamePlayer relacion2 = gamePlayerRepository.save(new GamePlayer(player2, juego01, LocalDateTime.now(), false));
            GamePlayer relacion3 = gamePlayerRepository.save(new GamePlayer(player1, juego02, juego02.getCreationTime(), true));
            GamePlayer relacion4 = gamePlayerRepository.save(new GamePlayer(player3, juego02, LocalDateTime.now(), false));
            GamePlayer relacion5 = gamePlayerRepository.save(new GamePlayer(player4, juego03, juego03.getCreationTime(), true));
            GamePlayer relacion6 = gamePlayerRepository.save(new GamePlayer(player5, juego03, LocalDateTime.now(), false));
            GamePlayer relacion7 = gamePlayerRepository.save(new GamePlayer(player2, juego04, juego04.getCreationTime(), true));
            GamePlayer relacion8 = gamePlayerRepository.save(new GamePlayer(player3, juego04, LocalDateTime.now(), false));

            Score score1 = scoreRepository.save(new Score(player3, juego04, LocalDateTime.now(), 1.0));
            Score score2 = scoreRepository.save(new Score(player2, juego04, LocalDateTime.now(), 1.0));
            Score score3 = scoreRepository.save(new Score(player4, juego03, LocalDateTime.now(), 0.5));
            Score score4 = scoreRepository.save(new Score(player5, juego03, LocalDateTime.now(), 0.5));

            relacion1.addShip(nave01);
            relacion1.addShip(nave02);
            relacion1.addShip(nave03);
            relacion1.addShip(nave04);
            relacion1.addShip(nave05);
            relacion2.addShip(nave06);
            relacion2.addShip(nave07);
            relacion2.addShip(nave08);
            relacion2.addShip(nave09);
            relacion2.addShip(nave10);

            Set<Positions> salvoesFire01 = new HashSet<>();
            Set<Positions> salvoesFire02 = new HashSet<>();
            salvoesFire01.add(Positions.E1);
            salvoesFire01.add(Positions.A1);
            salvoesFire01.add(Positions.G10);
            salvoesFire01.add(Positions.H7);
            salvoesFire01.add(Positions.C2);
            salvoesFire02.add(Positions.A9);
            salvoesFire02.add(Positions.E5);
            salvoesFire02.add(Positions.A5);
            salvoesFire02.add(Positions.B2);
            salvoesFire02.add(Positions.I6);
            salvoesFire02.add(Positions.B3);
            salvoesFire02.add(Positions.F9);

            Salvoes salvo01 = salvoesRepository.save(new Salvoes("Turno1", salvoesFire01));
            Salvoes salvo02 = salvoesRepository.save(new Salvoes("Turno1", salvoesFire02));

            relacion1.addSalvoes(salvo01);
            relacion2.addSalvoes(salvo02);

            gamePlayerRepository.save(relacion1);
            gamePlayerRepository.save(relacion2);
            gamePlayerRepository.save(relacion3);
            gamePlayerRepository.save(relacion4);
            gamePlayerRepository.save(relacion5);
        };
    }
}
