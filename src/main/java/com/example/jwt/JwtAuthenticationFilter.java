package com.example.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
/**
 * Her HTTP isteği için bir kez çalışan JWT filtre sınıfıdır.
 * Bu filtre, gelen isteklerde Authorization header içindeki JWT token'ı doğrular,
 * kullanıcı bilgilerini çözer ve kullanıcıyı Spring Security bağlamına yerleştirir.
 */
@Component// Spring Container tarafından bir bean olarak yönetilmesini sağlar
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    // JWT üretme ve doğrulama işlemlerini yöneten servis

    @Autowired
    private UserDetailsService userDetailsService;
    // Spring Security’nin kullanıcı bilgilerini yüklemek için kullandığı servis

    /**
     * Her HTTP isteğinde otomatik olarak tetiklenen filtre metodudur.
     * Token kontrolü, geçerlilik doğrulaması ve kullanıcı yetkilendirmesi burada yapılır.
     *
     * @param request  gelen HTTP isteği
     * @param response HTTP cevabı
     * @param filterChain filtre zinciri
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header;
        String token;
        String username;
        // Authorization başlığını alıyoruz (örnek: "Bearer eyJhbGc...")
        header = request.getHeader("Authorization");

        /**
         * Eğer Authorization header yoksa veya boşsa,
         * filtre zincirine devam edilir ve JWT doğrulama yapılmaz.
         */
        if(header==null){
            filterChain.doFilter(request,response);
            return;
        }

        // "Bearer " ifadesini keserek sadece token kısmını alıyoruz
        token = header.substring(7);

        // Token içindeki username bilgisini çözüyoruz (subject alanı)
        try {
            username = jwtService.getUsernameByToken(token);
            //Eğer username bulunduysa ve SecurityContext'te zaten authentication yoksa,kullanıcı doğrulaması yapılabilir.
            if (username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                // Veritabanından kullanıcı detaylarını çekiyoruz
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                /**
                 * Token geçerli ise kullanıcıyı SecurityContext'e dahil ediyoruz.
                 * NOT: isTokenExpired() → true dönerse token süresi DOLMUŞ demektir,
                 * bu yüzden "!" ile kullanılır.
                 */
                if(userDetails != null && !jwtService.isTokenExpired(token)){
                    // Authentication nesnesi oluşturuluyor
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username,null,userDetails.getAuthorities());
                    authentication.setDetails(userDetails);
                    // Authentication objesini SecurityContext'e ekliyoruz
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }catch (ExpiredJwtException e){
            System.out.println("Token süresi dolmuştur: " + e.getMessage());
        }
        catch (Exception e){
            System.out.println("Hata: " + e.getMessage());
        }
        /**
         * Filtre zincirinin devam etmesini sağlıyoruz.
         * Bu satır olmazsa istek controller'a ulaşamaz.
         */
        filterChain.doFilter(request,response);
    }
}
