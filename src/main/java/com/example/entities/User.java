package com.example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

/**
 * Uygulamadaki kullanıcı varlığını temsil eden JPA Entity sınıfıdır.
 * Aynı zamanda Spring Security tarafından kullanılan UserDetails arayüzünü
 * implemente ederek kimlik doğrulama sürecine entegre olur.
 */
@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {//UserDetail interfacesi hazır bir java interface
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    /**
     * Kullanıcının yetkilerini döner.
     * Roller eklenmek istenirse burada GrantedAuthority listesi oluşturulmalıdır.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
    /**
     * UserDetails arayüzü için zorunlu olan kullanıcı adı dönüşü.
     */
    @Override
    public String getUsername() {
        return this.userName;
    }
}
