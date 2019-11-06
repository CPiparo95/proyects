 package com.codeoftheweb.salvo.security;

import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.repositorys.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
    class WebSecurityConfigurationAuthentication extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    PlayerRepository playerRepo;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void init(AuthenticationManagerBuilder auth) throws  Exception{
        auth.userDetailsService(inputName ->{
            Player player = playerRepo.findByUsername(inputName); //preguntar que hace el metodo findByEmail, que dato va a buscar? sobre que valida?
            if (player != null) {
                return new User(player.getEmail(), player.getPassword(), //suponiendo que encuentre el supuesto dato, porque devuelve email y pass?
                        AuthorityUtils.createAuthorityList("USER")); //aca le genera un rol si lo encuntra? que pasa si lo encuentra 2 veces? sigue reasignandole el mismo rol
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        });
    }

    }


