package com.codeoftheweb.salvo.repositorys;

import com.codeoftheweb.salvo.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GameRepository extends JpaRepository<Game, Long> {
    Game findByGameName(@Param("gameName") String gameName);
}