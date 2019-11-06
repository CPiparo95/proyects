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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


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
                return new User(player.getUserName(), player.getPassword(), //suponiendo que encuentre el supuesto dato, porque devuelve email y pass?
                        AuthorityUtils.createAuthorityList("USER")); //aca le genera un rol si lo encuntra? que pasa si lo encuentra 2 veces? sigue reasignandole el mismo rol
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        });
    }



    }



 @Configuration
 @EnableWebSecurity
 class WebSecurityConfig extends WebSecurityConfigurerAdapter {

     @Override
     protected void configure(HttpSecurity http) throws Exception {
         http.authorizeRequests()
                 .antMatchers("/rest/**").hasAuthority("ADMIN")
                 .antMatchers("/api/game_view/**").hasAnyAuthority("USER","ADMIN")
                 .and()
                 .formLogin()
                 .usernameParameter("name")
                 .passwordParameter("pwd")
                 .loginPage("/app/login");
         http.logout().logoutUrl("/app/logout");

         // turn off checking for CSRF tokens
         http.csrf().disable();

         // if user is not authenticated, just send an authentication failure response
         http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

         // if login is successful, just clear the flags asking for authentication
         http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

         // if login fails, just send an authentication failure response
         http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

         // if logout is successful, just send a success response
         http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
     }

     private void clearAuthenticationAttributes(HttpServletRequest request) {
         HttpSession session = request.getSession(false);
         if (session != null) {
             session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
         }
     }
 }


