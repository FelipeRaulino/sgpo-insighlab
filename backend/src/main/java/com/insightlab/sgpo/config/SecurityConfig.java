package com.insightlab.sgpo.config;

import com.insightlab.sgpo.security.jwt.JWTFilter;
import com.insightlab.sgpo.security.jwt.JWTProvider;
import com.insightlab.sgpo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JWTProvider jwtProvider;
    private UserService userService;

    public SecurityConfig(JWTProvider jwtProvider){
        this.jwtProvider = jwtProvider;
    }

    @Autowired
    public void setUserService(@Lazy UserService userService){
        this.userService = userService;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManagerBean(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JWTFilter customFilter = new JWTFilter(jwtProvider, userService);

        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(
                        authorizeHttpRequest -> authorizeHttpRequest
                                .requestMatchers(
                                        "/auth/signin",
                                        "/auth/signup",
                                        "/auth/user",
                                        "/auth/refresh/**",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**"
                                ).permitAll()
                                // Apenas usuários ADMIN podem deletar suppliers e users
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/suppliers/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole("ADMIN")
                                // ADMIN e EMPLOYEE podem atualizar users
                                .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasAnyRole("ADMIN", "EMPLOYEE")
                                // Apenas usuários ADMIN têm acesso a todos os outros métodos de gerenciamento de users
                                .requestMatchers("/api/v1/users/**").hasRole("ADMIN")
                                // Para todos os outros métodos da API, o usuário deve ser ADMIN ou EMPLOYEE
                                .requestMatchers("/api/v1/**").hasAnyRole("ADMIN", "EMPLOYEE")
                                // Todas as outras requisições devem ser autenticadas
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> {})
                .authenticationProvider(authenticationProvider())
                .build();

    }

}
