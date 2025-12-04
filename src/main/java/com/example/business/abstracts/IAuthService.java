package com.example.business.abstracts;

import com.example.dto.DtoUser;
import com.example.jwt.AuthRequest;
import com.example.jwt.AuthResponse;

/**
 * Kimlik doğrulama (Authentication) ile ilgili iş mantığını yöneten servis arayüzüdür.
 * Kullanıcı kayıt ve  giriş işlemleri burada tanımlanır.
 */
public interface IAuthService {
    /**
     * Yeni bir kullanıcı kaydı oluşturur.
     *
     * @param request Kullanıcı kayıt bilgilerini içeren DTO nesnesi
     * @return Oluşturulan kullanıcı bilgilerini içeren DtoUser nesnesi
     */
    public DtoUser register(AuthRequest request);
    /**
     * Kullanıcının kimlik doğrulama (login) işlemini gerçekleştirir.
     * Kullanıcı adı ve parola doğrulanır. Doğrulama başarılı olursa JWT token üretilir
     * ve kullanıcıya AuthResponse formatında geri döndürülür.
     *
     * @param request Kullanıcının giriş bilgilerini içeren AuthRequest DTO'su
     * @return JWT token ve kullanıcı bilgilerini içeren AuthResponse nesnesi
     */
    public AuthResponse authenticate(AuthRequest request);
}
