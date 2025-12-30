package com.horrorcore.eva_social.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(htp ->
                                htp
                                        .requestMatchers(HttpMethod.GET, "/home", "/posts", "/posts/")
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated()
                        )
                .formLogin(form -> form
                        .defaultSuccessUrl("/posts", true)
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider);
        return http.build();
    }

}
