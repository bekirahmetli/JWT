package com.example.api.impl;

import com.example.api.IRestAuthController;
import com.example.business.abstracts.IAuthService;
import com.example.business.abstracts.IRefreshTokenService;
import com.example.dto.DtoUser;
import com.example.jwt.AuthRequest;
import com.example.jwt.AuthResponse;
import com.example.jwt.RefreshTokenRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kimlik doğrulama (Authentication) ile ilgili REST endpointlerini yöneten
 * controller sınıfıdır. IRestAuthController arayüzünü implemente eder.
 *
 * Bu sınıf, kullanıcı kayıt işlemlerini HTTP POST isteği ile gerçekleştirir.
 */
@RestController
public class RestAuthControllerImpl implements IRestAuthController {
    @Autowired
    private IAuthService authService;

    @Autowired
    private IRefreshTokenService refreshTokenService;
    /**
     * Yeni bir kullanıcı kaydı oluşturur.
     *
     * @param request HTTP isteği gövdesinde gönderilen kullanıcı kayıt bilgilerini içeren AuthRequest DTO'su.
     *                @Valid anotasyonu ile DTO içindeki validation kuralları uygulanır.
     * @return Oluşturulan kullanıcı bilgilerini içeren DtoUser nesnesi
     */
    @PostMapping("/register")
    @Override
    public DtoUser register(@Valid @RequestBody AuthRequest request) {
        return authService.register(request);
    }

    /**
     * Kullanıcının kimlik doğrulama (login) işlemini gerçekleştirir.
     * Kullanıcı adı ve parolası doğrulanır; başarılı olması durumunda JWT token oluşturulur ve kullanıcıya döndürülür.
     *
     * @param request HTTP isteği gövdesinde gönderilen kimlik doğrulama bilgilerini içeren AuthRequest DTO'su.
     * @return JWT token ve kullanıcı bilgilerini içeren AuthResponse nesnesi
     */
    @PostMapping("/authenticate")
    @Override
    public AuthResponse authenticate(@Valid @RequestBody AuthRequest request) {
        return authService.authenticate(request);
    }
    /**
     * Kullanıcının mevcut refresh token'ı ile yeni bir access token üretmesini sağlar.
     * Refresh token doğrulanır ve süresi geçmemişse yeni JWT access token oluşturulur.
     *
     * @param request HTTP isteği gövdesinde gönderilen RefreshTokenRequest nesnesi
     * @return Yeni access token ve mevcut/yenilenmiş refresh token bilgisini içeren AuthResponse nesnesi
     */
    @PostMapping("/refreshToken")
    @Override
    public AuthResponse refreshToken(@RequestBody RefreshTokenRequest request) {
        return refreshTokenService.refreshToken(request);
    }
}
