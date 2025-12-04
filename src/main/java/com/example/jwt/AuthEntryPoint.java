package com.example.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * Yetkisiz erişim (401 Unauthorized) durumlarında çalışacak olan
 * özel bir AuthenticationEntryPoint implementasyonudur.
 *
 * Bu sınıf, Spring Security tarafından korunan bir kaynağa
 * kimliği doğrulanmamış bir kullanıcı erişmeye çalıştığında devreye girer.
 */
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
    /**
     * Yetkisiz bir istek gerçekleştiğinde tetiklenir.
     * Kullanıcı doğrulanmamışsa HTTP 401 hatası döner.
     *
     * @param request  Gelen HTTP isteği
     * @param response HTTP yanıt nesnesi
     * @param authException Kimlik doğrulama hatası hakkında bilgi içerir
     * @throws IOException Giriş/çıkış hatalarında fırlatılır
     * @throws ServletException Servlet işlem hatalarında fırlatılır
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,authException.getMessage());
    }
}
