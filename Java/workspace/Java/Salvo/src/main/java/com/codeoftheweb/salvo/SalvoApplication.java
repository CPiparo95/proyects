package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.codeoftheweb.salvo.repositorys.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@SpringBootApplication
public class SalvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class);
    }

    @Bean
    public CommandLineRunner initData(
            PlayerRepository playerRepository,
            GameRepository gameRepository,
            GamePlayerRepository gamePlayerRepository,
            ShipRepository shipRepository) {
        return (args) -> {
            // save a couple of players
            Player player1 = playerRepository.save(new Player("Jack", "nosoyhorrible"));
            Player player2 = playerRepository.save(new Player("Chloe", "soyUnapassword"));
            Player player3 = playerRepository.save(new Player("Diana", "soyUnapassword"));
            Player player4 = playerRepository.save(new Player("Amelia", "soyUnapassword"));
            Player player5 = playerRepository.save(new Player("Rojelio", "soyUnapassword"));

            Game juego01 = gameRepository.save(new Game("Juego nuevo01", LocalDateTime.now()));
            Game juego02 = gameRepository.save(new Game("Juevo nuevo02", LocalDateTime.now()));

            Set<ShipPositions> barco1 = new TreeSet<>();
            Set<ShipPositions> barco2 = new TreeSet<>();
            Set<ShipPositions> barco3 = new TreeSet<>();
            Set<ShipPositions> barco4 = new TreeSet<>();
            Set<ShipPositions> barco5 = new TreeSet<>();
            barco1.add(ShipPositions.A1);
            barco1.add(ShipPositions.A2);
            barco1.add(ShipPositions.A3);
            barco1.add(ShipPositions.A4);
            barco2.add(ShipPositions.C1);
            barco2.add(ShipPositions.C2);
            barco2.add(ShipPositions.C3);
            barco3.add(ShipPositions.D1);
            barco3.add(ShipPositions.D2);
            barco4.add(ShipPositions.F1);
            barco4.add(ShipPositions.F2);
            barco4.add(ShipPositions.F3);
            barco4.add(ShipPositions.F4);
            barco4.add(ShipPositions.F5);
            barco5.add(ShipPositions.H1);
            barco5.add(ShipPositions.H2);
            barco5.add(ShipPositions.H3);
            barco5.add(ShipPositions.H4);

            Ship nave01 = shipRepository.save(new Ship(ShipType.DESTROYER,barco1 ));
            Ship nave02 = shipRepository.save(new Ship(ShipType.AIRCRAFT_CARRIER,barco2));
            Ship nave03 = shipRepository.save(new Ship(ShipType.ARMOIRED_CRUISER,barco3));
            Ship nave04 = shipRepository.save(new Ship(ShipType.DESTROYER_FLOTILLA_LEADER,barco4));
            Ship nave05 = shipRepository.save(new Ship(ShipType.SUBMARINE,barco5));
            Ship nave06 = shipRepository.save(new Ship(ShipType.HEAVY_CRUISER,barco1));
            Ship nave07 = shipRepository.save(new Ship(ShipType.AMPHIBIOUS_ASSAULT_SHIP,barco2));
            Ship nave08 = shipRepository.save(new Ship(ShipType.DESTROYER,barco3));

            GamePlayer relacion1 = gamePlayerRepository.save(new GamePlayer(player1, juego01, LocalDateTime.now()));
            GamePlayer relacion2 = gamePlayerRepository.save(new GamePlayer(player2, juego01, LocalDateTime.now()));
            GamePlayer relacion5 = gamePlayerRepository.save(new GamePlayer(player1, juego02, LocalDateTime.now()));
            GamePlayer relacion3 = gamePlayerRepository.save(new GamePlayer(player3, juego02, LocalDateTime.now()));
            GamePlayer relacion4 = gamePlayerRepository.save(new GamePlayer(player4, juego02, LocalDateTime.now()));
            relacion1.addShip(nave01);
            relacion1.addShip(nave02);
            relacion2.addShip(nave03);
            relacion2.addShip(nave04);
            relacion3.addShip(nave05);
            relacion3.addShip(nave06);
            relacion4.addShip(nave07);
            relacion4.addShip(nave08);

            gamePlayerRepository.save(relacion1);
            gamePlayerRepository.save(relacion2);
            gamePlayerRepository.save(relacion3);
            gamePlayerRepository.save(relacion4);
            gamePlayerRepository.save(relacion5);
        };
    }
}
