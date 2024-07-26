package com.security_service.security_service_employee.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private AuthenticationProvider authenticationProvider;
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfiguration(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                  .authorizeHttpRequests(request -> request
                            .requestMatchers("/authentication/**").permitAll()
                            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                            .anyRequest().authenticated()
                  )

                  .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                  .authenticationProvider(authenticationProvider)
                  .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }


}