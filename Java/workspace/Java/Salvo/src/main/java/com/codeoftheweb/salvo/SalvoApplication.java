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

            Set<ShipPositions> barco1 = new HashSet<>();
            Set<ShipPositions> barco2 = new HashSet<>();
            Set<ShipPositions> barco3 = new HashSet<>();
            Set<ShipPositions> barco4 = new HashSet<>();
            Set<ShipPositions> barco5 = new HashSet<>();
            Set<ShipPositions> barco6 = new HashSet<>();
            Set<ShipPositions> barco7 = new HashSet<>();
            Set<ShipPositions> barco8 = new HashSet<>();
            Set<ShipPositions> barco9 = new HashSet<>();
            Set<ShipPositions> barco10 = new HashSet<>();
            barco1.add(ShipPositions.A1);
            barco1.add(ShipPositions.B1);
            barco1.add(ShipPositions.C1);
            barco1.add(ShipPositions.D1);
            barco2.add(ShipPositions.E8);
            barco2.add(ShipPositions.F8);
            barco2.add(ShipPositions.G8);
            barco3.add(ShipPositions.D3);
            barco3.add(ShipPositions.D4);
            barco4.add(ShipPositions.A4);
            barco4.add(ShipPositions.B4);
            barco4.add(ShipPositions.C4);
            barco4.add(ShipPositions.D4);
            barco4.add(ShipPositions.E4);
            barco5.add(ShipPositions.A7);
            barco5.add(ShipPositions.A8);
            barco5.add(ShipPositions.A9);
            barco5.add(ShipPositions.A10);
            barco6.add(ShipPositions.C1);
            barco6.add(ShipPositions.D1);
            barco6.add(ShipPositions.E1);
            barco6.add(ShipPositions.F1);
            barco7.add(ShipPositions.C8);
            barco7.add(ShipPositions.D8);
            barco7.add(ShipPositions.E8);
            barco8.add(ShipPositions.A3);
            barco8.add(ShipPositions.A4);
            barco9.add(ShipPositions.B4);
            barco9.add(ShipPositions.C4);
            barco9.add(ShipPositions.D4);
            barco9.add(ShipPositions.E4);
            barco9.add(ShipPositions.F4);
            barco10.add(ShipPositions.A7);
            barco10.add(ShipPositions.A8);
            barco10.add(ShipPositions.A9);
            barco10.add(ShipPositions.A10);

            Ship nave01 = shipRepository.save(new Ship(ShipType.gaucho1,barco4));
            Ship nave02 = shipRepository.save(new Ship(ShipType.gaucho2,barco3));
            Ship nave03 = shipRepository.save(new Ship(ShipType.gaucho3,barco3));
            Ship nave04 = shipRepository.save(new Ship(ShipType.gaucho4,barco4));
            Ship nave05 = shipRepository.save(new Ship(ShipType.gaucho5,barco5));
            Ship nave06 = shipRepository.save(new Ship(ShipType.gaucho1,barco6));
            Ship nave07 = shipRepository.save(new Ship(ShipType.gaucho2,barco7));
            Ship nave08 = shipRepository.save(new Ship(ShipType.gaucho3,barco8));
            Ship nave09 = shipRepository.save(new Ship(ShipType.gaucho4,barco9));
            Ship nave10 = shipRepository.save(new Ship(ShipType.gaucho5,barco10));

            GamePlayer relacion1 = gamePlayerRepository.save(new GamePlayer(player1, juego01, LocalDateTime.now()));
            GamePlayer relacion2 = gamePlayerRepository.save(new GamePlayer(player2, juego01, LocalDateTime.now()));
            GamePlayer relacion5 = gamePlayerRepository.save(new GamePlayer(player1, juego02, LocalDateTime.now()));
            GamePlayer relacion3 = gamePlayerRepository.save(new GamePlayer(player3, juego02, LocalDateTime.now()));
            GamePlayer relacion4 = gamePlayerRepository.save(new GamePlayer(player4, juego02, LocalDateTime.now()));
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

            gamePlayerRepository.save(relacion1);
            gamePlayerRepository.save(relacion2);
            gamePlayerRepository.save(relacion3);
            gamePlayerRepository.save(relacion4);
            gamePlayerRepository.save(relacion5);
        };
    }
}
