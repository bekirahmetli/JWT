package com.example.config;

import com.example.dao.UserRepo;
import com.example.entities.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;

/**
 * Uygulamanın genel yapılandırmalarını içeren Spring Configuration sınıfıdır.
 * Bu sınıfta UserDetailsService, AuthenticationManager ve PasswordEncoder bean'leri tanımlanmıştır.
 */
@Configuration
public class AppConfig {

    private final UserRepo userRepo;

    public AppConfig(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * UserDetailsService bean tanımıdır.
     * Spring Security, kimlik doğrulama sürecinde kullanıcı bilgilerini bu servis aracılığıyla yükler.
     *
     * @return UserDetailsService implementasyonu
     */
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                //Kullanıcı adıyla veritabanından kullanıcıyı arıyoruz.Eğer kullanıcı bulunamazsa UsernameNotFoundException fırlatılır.
                Optional<User> optional = userRepo.findByUsername(username);
                if(optional.isPresent()){
                    return optional.get();
                }
                return optional.orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    /**
     * AuthenticationManager bean'ini sağlar.
     * Spring Security 6 ile birlikte AuthenticationManager doğrudan enjekte edilemediği için
     * AuthenticationConfiguration üzerinden alınması gerekir.
     *
     * @param configuration Spring Security ayarlarını içeren yapılandırma nesnesi
     * @return AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    /**
     * Uygulamada kullanılacak BCryptPasswordEncoder bean tanımıdır.
     * Parola hashing işlemleri için endüstri standardı güçlü bir algoritma sağlar.
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
