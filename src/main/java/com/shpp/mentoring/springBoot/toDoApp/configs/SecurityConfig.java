package com.shpp.mentoring.springBoot.toDoApp.configs;

import com.shpp.mentoring.springBoot.toDoApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
//новая модель конфигурирования через бины, не нужно экстендить WebSecurityConfigurerAdapter
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig  {
   private final UserService userService;
   private final JwtRequestFilter jwtRequestFilter;
//следующие 3 метода (daoAuthenticationProvider, passwordEncoder, authenticationManager ) - это базовая настройка для того чтобы
// у нас были пользователи, были роли, база данных использовалась в качестве источника, был пассвордэнкодер для работы с паролями

   @Bean
   public DaoAuthenticationProvider daoAuthenticationProvider(){
       DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
       daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
       daoAuthenticationProvider.setUserDetailsService(userService);
       return daoAuthenticationProvider;
   }

   @Bean
    public BCryptPasswordEncoder passwordEncoder(){
       return new BCryptPasswordEncoder();
   }

   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
       return authenticationConfiguration.getAuthenticationManager();
   }
    //настраиваем правила безопасности
   @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
       http
               .csrf().disable()
               .cors().disable()
               //устанавливаем какие запросы пропускать а какие не пропускать
               .authorizeRequests()
              // .antMatchers("/secured").authenticated()
               .antMatchers("/info").authenticated()
               .antMatchers("/admin").hasRole("ADMIN")
               .anyRequest().permitAll()
               .and()
               .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               .and()
               .exceptionHandling()
               .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
               .and().addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
       return http.build();
   }




}
