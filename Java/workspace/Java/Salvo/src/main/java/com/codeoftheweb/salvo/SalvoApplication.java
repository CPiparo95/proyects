package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.codeoftheweb.salvo.repositorys.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
            ShipRepository shipRepository,
            SalvoesRepository salvoesRepository) {
        return (args) -> {
            // save a couple of players
            Player player1 = playerRepository.save(new Player("Jack", "nosoyhorrible"));
            Player player2 = playerRepository.save(new Player("Chloe", "soyUnapassword"));
            Player player3 = playerRepository.save(new Player("Diana", "soyUnapassword"));
            Player player4 = playerRepository.save(new Player("Amelia", "soyUnapassword"));
            Player player5 = playerRepository.save(new Player("Rojelio", "soyUnapassword"));

            Game juego01 = gameRepository.save(new Game("Juego nuevo01", LocalDateTime.now()));
            Game juego02 = gameRepository.save(new Game("Juevo nuevo02", LocalDateTime.now()));

            Set<Positions> barco1 = new HashSet<>();
            Set<Positions> barco2 = new HashSet<>();
            Set<Positions> barco3 = new HashSet<>();
            Set<Positions> barco4 = new HashSet<>();
            Set<Positions> barco5 = new HashSet<>();
            Set<Positions> barco6 = new HashSet<>();
            Set<Positions> barco7 = new HashSet<>();
            Set<Positions> barco8 = new HashSet<>();
            Set<Positions> barco9 = new HashSet<>();
            Set<Positions> barco10 = new HashSet<>();
            barco1.add(Positions.A1);
            barco1.add(Positions.B1);
            barco1.add(Positions.C1);
            barco1.add(Positions.D1);
            barco2.add(Positions.E8);
            barco2.add(Positions.F8);
            barco2.add(Positions.G8);
            barco3.add(Positions.D3);
            barco3.add(Positions.D4);
            barco4.add(Positions.A4);
            barco4.add(Positions.B4);
            barco4.add(Positions.C4);
            barco4.add(Positions.D4);
            barco4.add(Positions.E4);
            barco5.add(Positions.A7);
            barco5.add(Positions.A8);
            barco5.add(Positions.A9);
            barco5.add(Positions.A10);
            barco6.add(Positions.C1);
            barco6.add(Positions.D1);
            barco6.add(Positions.E1);
            barco6.add(Positions.F1);
            barco7.add(Positions.C8);
            barco7.add(Positions.D8);
            barco7.add(Positions.E8);
            barco8.add(Positions.A3);
            barco8.add(Positions.A4);
            barco9.add(Positions.B4);
            barco9.add(Positions.C4);
            barco9.add(Positions.D4);
            barco9.add(Positions.E4);
            barco9.add(Positions.F4);
            barco10.add(Positions.A7);
            barco10.add(Positions.A8);
            barco10.add(Positions.A9);
            barco10.add(Positions.A10);

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

            Set<Positions> salvoesFire01 = new HashSet<>();
            Set<Positions> salvoesFire02 = new HashSet<>();
            salvoesFire01.add(Positions.E1);
            salvoesFire01.add(Positions.A1);
            salvoesFire01.add(Positions.G10);
            salvoesFire01.add(Positions.H7);
            salvoesFire01.add(Positions.C2);
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
