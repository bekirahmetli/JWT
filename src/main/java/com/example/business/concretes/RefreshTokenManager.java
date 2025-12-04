package com.example.business.concretes;

import com.example.business.abstracts.IRefreshTokenService;
import com.example.dao.RefreshTokenRepo;
import com.example.entities.RefreshToken;
import com.example.entities.User;
import com.example.jwt.AuthResponse;
import com.example.jwt.JwtService;
import com.example.jwt.RefreshTokenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * Refresh token yönetim servisidir.
 * IRefreshTokenService arayüzünü implemente ederek kullanıcıların
 * refresh token ile yeni access token almasını sağlar.
 */
@Service
public class RefreshTokenManager implements IRefreshTokenService {
    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private JwtService jwtService;

    /**
     * Verilen refresh token süresinin dolup dolmadığını kontrol eder.
     *
     * @param expiredDate Refresh token'ın son geçerlilik tarihi
     * @return Token henüz geçerliyse true, süresi dolmuşsa false
     */
    public boolean isRefreshTokenExpired(Date expiredDate){
        return new Date().before(expiredDate); // Mevcut tarih expiry tarihinden önce mi?
    }

    /**
     * Kullanıcıya ait yeni bir refresh token oluşturur.
     *
     * @param user Token üretilecek kullanıcı
     * @return Yeni oluşturulmuş RefreshToken entity nesnesi
     */
    private RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(UUID.randomUUID().toString()); // Rastgele bir refresh token değeri oluşturulur
        refreshToken.setExpireDate(new Date(System.currentTimeMillis() + 1000*60*60*4));// Refresh token 4 saat geçerli olacak şekilde expire süresi ayarlanır
        refreshToken.setUser(user);// Token hangi kullanıcıya ait ise ilişkilendirilir
        return refreshToken;
    }

    /**
     * Kullanıcının geçerli refresh token ile yeni bir access token almasını sağlar.
     * Refresh token doğrulanır, süresi geçmemişse yeni access token üretilir.
     * Aynı zamanda yeni bir refresh token da oluşturulur ve kaydedilir.
     *
     * @param request Kullanıcının refresh token bilgisini içeren RefreshTokenRequest nesnesi
     * @return Yeni access token ve refresh token bilgilerini içeren AuthResponse nesnesi
     */
    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        Optional<RefreshToken> optional = refreshTokenRepo.findByRefreshToken(request.getRefreshToken());
        if (optional.isEmpty()){
            System.out.println("Refresh Token Geçersizdir!"+request.getRefreshToken());
        }
        RefreshToken refreshToken = optional.get();
        if (!isRefreshTokenExpired(refreshToken.getExpireDate())){
            System.out.println("Refresh Token expire olmuş"+request.getRefreshToken());
        }
        String accessToken = jwtService.generateToken(refreshToken.getUser());// Yeni access token oluşturulur
        RefreshToken savedRefreshToken = refreshTokenRepo.save(createRefreshToken(refreshToken.getUser()));// Yeni refresh token oluşturulur ve kaydedilir
        return new AuthResponse(accessToken,savedRefreshToken.getRefreshToken());// Access token + refresh token response olarak döndürülür
    }
}
