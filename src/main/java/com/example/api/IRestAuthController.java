package com.example.api;

import com.example.dto.DtoUser;
import com.example.jwt.AuthRequest;
import com.example.jwt.AuthResponse;
import com.example.jwt.RefreshTokenRequest;

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

    /**
     * Kullanıcının kimlik doğrulama (login) işlemini gerçekleştiren endpoint metodudur.
     * Kullanıcı adı ve parola kontrol edilir ve başarılı ise JWT token üretilir.
     *
     * @param request Kullanıcı giriş bilgilerini içeren DTO nesnesi
     * @return JWT token ve kullanıcı bilgilerini içeren AuthResponse nesnesi
     */
    public AuthResponse authenticate(AuthRequest request);
    /**
     * Kullanıcının mevcut refresh token'ı ile yeni bir access token üretmesini sağlayan endpoint metodudur.
     * Refresh token doğrulanır, süresi geçmemişse yeni JWT (access token) oluşturulup döndürülür.
     *
     * @param request Kullanıcının refresh token bilgisini içeren RefreshTokenRequest nesnesi
     * @return Yeni access token ve mevcut/yenilenmiş refresh token bilgisini içeren AuthResponse nesnesi
     */
    public AuthResponse refreshToken(RefreshTokenRequest request);
}
