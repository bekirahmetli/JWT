package com.example.api.impl;

import com.example.api.IRestAuthController;
import com.example.business.abstracts.IAuthService;
import com.example.dto.DtoUser;
import com.example.jwt.AuthRequest;
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
}
