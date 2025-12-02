package com.example.business.abstracts;

import com.example.dto.DtoUser;
import com.example.jwt.AuthRequest;

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
}
