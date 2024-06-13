package com.mirna.busmanagement.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        (authorize) -> authorize.requestMatchers(new AntPathRequestMatcher("/home"),
                                        new AntPathRequestMatcher("/"),
                                        new AntPathRequestMatcher("/registration"),
                                        new AntPathRequestMatcher("/routes/search"),
                                        new AntPathRequestMatcher("/static/**"),
                                        new AntPathRequestMatcher("/images/**"),
                                        new AntPathRequestMatcher("/styles/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/routes"),
                                        new AntPathRequestMatcher("/problem")).hasAnyAuthority("ROLE_DRIVER", "ROLE_ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/users/**"),
                                        new AntPathRequestMatcher("/routes/**"),
                                        new AntPathRequestMatcher("/buses/**"),
                                        new AntPathRequestMatcher("/location/**")).hasAuthority("ROLE_ADMIN")
                                .anyRequest().authenticated())
                .formLogin(login -> login
                        .usernameParameter("username")
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/logout").permitAll()
                        .logoutSuccessUrl("/home"));

        return http.build();

    }


}