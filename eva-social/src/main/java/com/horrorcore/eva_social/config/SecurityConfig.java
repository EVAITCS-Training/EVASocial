package com.horrorcore.eva_social.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private AuthenticationProvider authenticationProvider;
    
    @Autowired
    private JwtAuthFilter authFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{
        httpSecurity
        		.cors(cor -> cor.configurationSource(corConfig()))
        		.csrf(cs -> cs.disable())
                .authorizeHttpRequests(http ->http
                        .requestMatchers(
                        		"/api/v1/auth/register",
                        		"/api/v1/auth/login",
                                "/actuator/**",
                                "/",
                                "/static/**",
                                "/assets/**",
                                "/favicon.ico",
                                "index.html",
                                "/home",
                                "/posts",
                                "/posts/"
                        		)
                        .permitAll()
                        .requestMatchers("/api/v1/**")
                        .authenticated()
                        .anyRequest()
                        .permitAll()
                        )
                .sessionManagement(ses -> ses.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
    
    @Bean
    protected UrlBasedCorsConfigurationSource corConfig() {
    	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    	CorsConfiguration configuration = new CorsConfiguration();
    	configuration.setAllowCredentials(false);
    	configuration.addAllowedOrigin("http://localhost:5173");
    	configuration.addAllowedOrigin("http://localhost:3000");
    	configuration.addAllowedHeader("*");
    	configuration.addAllowedMethod("*");
    	source.registerCorsConfiguration("/**", configuration);
    	return source;
    }
}
