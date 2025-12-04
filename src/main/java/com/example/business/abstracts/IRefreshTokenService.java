package com.example.business.abstracts;

import com.example.jwt.AuthResponse;
import com.example.jwt.RefreshTokenRequest;

/**
 * Refresh token işlemlerini yöneten servis arayüzüdür.
 * Bu arayüz, kullanıcının geçerli refresh token ile yeni bir access token
 * almasını sağlayan metodu tanımlar.
 */
public interface IRefreshTokenService {
    /**
     * Kullanıcının gönderdiği refresh token'ı doğrular ve geçerliyse
     * yeni bir access token üretir.
     *
     * @param request Kullanıcının refresh token bilgisini içeren RefreshTokenRequest nesnesi
     * @return Yeni access token ve refresh token bilgilerini içeren AuthResponse nesnesi
     */
    public AuthResponse refreshToken(RefreshTokenRequest request);
}
