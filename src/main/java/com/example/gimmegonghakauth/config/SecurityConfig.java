package com.example.gimmegonghakauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                .requestMatchers("/api/user/withdrawal", "/api/user/change/password", "/api/user/logout",
                    "/api/excel/**", "/api/gonghak/**","/api/user").authenticated()
                .anyRequest().permitAll()
            );
        http
            .formLogin((auth) -> auth.loginPage("/api/user/login")
                .defaultSuccessUrl("/api/user")
                .permitAll()
            ); //로그인시 메인페이지로 되돌아감
        http
            .logout((auth) -> auth.logoutUrl("/api/user/logout")
                .logoutSuccessUrl("/")
            );//로그아웃시 메인페이지로 되돌아감
        http
            .csrf(
                (auth) -> auth.csrfTokenRepository((CookieCsrfTokenRepository.withHttpOnlyFalse()))
                    .ignoringRequestMatchers(
                        "/api/user/send-verification-email") // "/user/send-verification-email" 경로에 대한 CSRF 보안 비활성화
                    .ignoringRequestMatchers(
                        "/api/user/verify-code") // "/user/verify-code" 경로에 대한 CSRF 보안 비활성화
                    .ignoringRequestMatchers("/api/user/certification/clear")
                    .ignoringRequestMatchers("/api/user/verify-status")
            );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
