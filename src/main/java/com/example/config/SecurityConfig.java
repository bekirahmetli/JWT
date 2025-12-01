package com.example.config;

import com.example.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Uygulamanın güvenlik yapılandırmalarını yöneten sınıftır.
 * Spring Security 6 ile uyumlu olarak SecurityFilterChain bean’i tanımlar.
 * Bu sınıf sayesinde:
 * - Hangi URL’lerin yetkisiz erişime açık olduğu,
 * - Hangi URL’lerin yetkilendirmeye tabi olduğu,
 * - JWT filtreleri,
 * - AuthenticationProvider yapılandırmaları
 * belirlenir.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //Kullanıcı giriş (authentication) URL'si
    public static final String AUTHENTICATE = "/authenticate";
    //Kullanıcı kayıt (register) URL'si
    public static final String REGISTER = "/register";

    @Autowired
    private AuthenticationProvider authenticationProvider;
    // Kullanıcı kimlik doğrulamasını gerçekleştirecek AuthenticationProvider
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    // JWT doğrulama ve filtreleme işlemleri için özel filtre


    /**
     * Spring Security Filter Chain bean tanımı.
     * HTTP güvenlik kuralları burada yapılandırılır.
     *
     * @param http HttpSecurity yapılandırması
     * @return SecurityFilterChain
     * @throws Exception olası konfigürasyon hatalarında fırlatılır
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http){
        http.csrf(csrf -> csrf.disable())// CSRF korumasını devre dışı bırakıyoruz (REST API için tipik)
                .authorizeHttpRequests(auth -> auth// URL bazlı yetkilendirme kuralları
                        .requestMatchers(AUTHENTICATE, REGISTER).permitAll()// Giriş ve kayıt URL'leri herkese açık
                        .anyRequest().authenticated() // Diğer tüm istekler kimlik doğrulaması gerektirir
                )  // Session yönetimini stateless olarak ayarlıyoruz (JWT için gereklidir)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider) // Authentication işlemleri için özel provider
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);// JWT filter’ını UsernamePasswordAuthenticationFilter'dan önce ekliyoruz

        return http.build();
    }
}
