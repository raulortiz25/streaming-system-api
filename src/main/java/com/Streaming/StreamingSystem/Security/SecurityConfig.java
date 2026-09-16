package com.Streaming.StreamingSystem.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){

        return httpSecurity
                .csrf(csrf-> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http ->{http
                        .requestMatchers("/auth/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/movie/**" ).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/movie/**" ).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/movie/**" ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/movie/**").authenticated();
                })
                .addFilterBefore(new JwtTokenValidator(jwtUtils, objectMapper), UsernamePasswordAuthenticationFilter.class)
                .build();



    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
        return authenticationConfiguration.getAuthenticationManager();

    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
