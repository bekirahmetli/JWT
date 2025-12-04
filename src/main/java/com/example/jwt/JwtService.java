package com.example.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT oluşturma ve doğrulama işlemlerini yöneten servis sınıfıdır.
 * Bu sınıf Spring tarafından bir bean olarak yönetilir.
 */
@Component
public class JwtService {
    /**
     * JWT imzalama işleminde kullanılacak gizli anahtar (Base64 formatında).
     * Güvenlik açısından gerçek projelerde .env veya Config Server üzerinden sağlanmalıdır.
     */
    public static final String SECRET_KEY = "pdj9UniPhUf/ctg9Lb7hO0XpkQVz8hVGYEXGYziC4fY=";

    /**
     * Kullanıcı bilgilerine göre JWT üretir.
     * Token oluşturma metodu
     * @param userDetails Spring Security'nin UserDetails implementasyonu
     * @return imzalanmış JWT Token
     */
    public String generateToken(UserDetails userDetails){
        Map<String,String> claimsMap = new HashMap<>();
        claimsMap.put("role","ADMIN");// Örnek amaçlı role claim’i eklenmiştir.

        return Jwts.builder()
                .claims(claimsMap)// Token içerisine custom claims eklenir.
                .setSubject(userDetails.getUsername())// Token sahibi (subject)
                .setIssuedAt(new Date())//Token oluşturma zamanı
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*2))// Token geçerlilik süresi (2 saat)
                .signWith(getKey(), SignatureAlgorithm.HS256)// HS256 algoritması ile imzalama
                .compact();// Token'ı string formatında oluşturur
    }

    /**
     * Verilen JWT token içerisindeki belirli bir Claim değerini döndürür.
     *
     * @param token JWT token
     * @param key    Claim anahtarı
     * @return İlgili Claim değeri
     */
    public Object getClaimsByKey(String token,String key){
        Claims claims = getClaims(token);
        return claims.get(key);
    }

    /**
     * Verilen JWT token'ı çözerek içindeki tüm Claim değerlerini döndürür.
     *
     * @param token JWT token
     * @return Token içerisindeki Claims gövdesi
     */
    public Claims getClaims(String token){
        Claims claims = Jwts
                .parser()// Parser oluşturulur
                .setSigningKey(getKey())// Token doğrulaması için imzalama anahtarı eklenir
                .build() // Parser instance oluşturulur
                .parseClaimsJws(token)// Token parse edilir ve imzalı claim’ler alınır
                .getBody();// Claims gövdesi alınır
        return claims;
    }


    /**
     * Token içerisindeki Claim'leri çözer ve verilen fonksiyon aracılığıyla
     * istenen türde bir sonuç döndürür.
     */
    public <T> T exportToken(String token, Function<Claims, T> claimsTFunction){
        Claims claims = getClaims(token);
        return claimsTFunction.apply(claims);
    }

    /**
     * Token içerisinden username (subject) bilgisini alır.
     *
     * @param token JWT Token
     * @return String - username
     */
    public String getUsernameByToken(String token){
        return exportToken(token,Claims::getSubject);
    }

    /**
     * Token'ın süresinin dolup dolmadığını kontrol eder.
     *
     * @param token JWT Token
     * @return true -> Token süresi dolmuş, false -> Geçerli
     */
    public boolean isTokenExpired(String token){
        Date expiredDate = exportToken(token,Claims::getExpiration);
        return new Date().after(expiredDate);
    }

    /**
     * Base64 ile encode edilmiş SECRET_KEY değerini çözerek
     * HMAC-SHA256 algoritması için uygun bir SecretKey nesnesi üretir.
     *
     * @return Key - imzalama anahtarı
     */
    public Key getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
