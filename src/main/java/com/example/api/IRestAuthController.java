package com.example.api;

import com.example.dto.DtoUser;
import com.example.jwt.AuthRequest;

/**
 * Kimlik doğrulama (Authentication) ile ilgili HTTP endpoint'lerini tanımlayan
 * controller arayüzüdür. Uygulamada bu interface'i implemente eden controller sınıfı,
 * kullanıcı kayıt ve giriş işlemlerini yönetir.
 */
public interface IRestAuthController {
    /**
     * Yeni bir kullanıcı kaydı oluşturan endpoint metodudur.
     *
     * @param request Kullanıcı kayıt bilgilerini içeren DTO nesnesi
     * @return Oluşturulan kullanıcı bilgilerini içeren DtoUser nesnesi
     */
    public DtoUser register(AuthRequest request);
}
