package com.example.business.concretes;

import com.example.business.abstracts.IAuthService;
import com.example.dao.UserRepo;
import com.example.dto.DtoUser;
import com.example.entities.User;
import com.example.jwt.AuthRequest;
import com.example.jwt.AuthResponse;
import com.example.jwt.JwtService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Kimlik doğrulama (Authentication) ile ilgili iş mantığını gerçekleştiren servis sınıfıdır.
 * IAuthService arayüzünü implemente eder.
 * Kullanıcı kayıt işlemleri burada gerçekleştirilir.
 */
@Service
public class AuthManager implements IAuthService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtService jwtService;
    /**
     * Yeni bir kullanıcı kaydı oluşturur.
     *
     * @param request Kullanıcı kayıt bilgilerini içeren AuthRequest DTO'su
     * @return Oluşturulan kullanıcı bilgilerini içeren DtoUser DTO'su
     */
    @Override
    public DtoUser register(AuthRequest request) {
        DtoUser dtoUser = new DtoUser();
        User user = new User();

        user.setUserName(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepo.save(user);
        BeanUtils.copyProperties(savedUser,dtoUser);
        return dtoUser;
    }

    /**
     * Kullanıcıyı kimlik doğrulamadan geçirir.
     * Kullanıcı adı ve parola Spring Security AuthenticationProvider ile doğrulanır.
     * Doğrulama başarılı ise kullanıcı bilgilerine göre JWT token üretilir.
     *
     * @param request Kullanıcının giriş bilgilerini (username + password) içeren AuthRequest DTO'su
     * @return JWT token bilgisini içeren AuthResponse nesnesi
     * @throws RuntimeException Kullanıcı adı veya şifre hatalı olduğunda fırlatılır
     */
    @Override
    public AuthResponse authenticate(AuthRequest request) {
        try {
            // Kullanıcıdan gelen username + password bilgileri ile güvenlik token'ı oluşturulur
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            authenticationProvider.authenticate(auth); // Spring Security doğrulama işlemi (şifre kontrolü burada gerçekleşir)

            Optional<User> optionalUser = userRepo.findByUserName(request.getUsername()); // Kullanıcı veritabanında aranır

            String token = jwtService.generateToken(optionalUser.get());// Kullanıcı bulunduysa token oluşturulur
            return new AuthResponse(token);// JWT token response olarak döndürülür
        } catch (Exception e) {
            throw new RuntimeException("Kullanıcı adı veya şifre hatalı", e);
        }
    }
}
