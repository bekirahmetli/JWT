package com.example.business.concretes;

import com.example.business.abstracts.IAuthService;
import com.example.dao.UserRepo;
import com.example.dto.DtoUser;
import com.example.entities.User;
import com.example.jwt.AuthRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Kimlik doğrulama (Authentication) ile ilgili iş mantığını gerçekleştiren servis sınıfıdır.
 * IAuthService arayüzünü implemente eder.
 * Kullanıcı kayıt işlemleri burada gerçekleştirilir.
 */
@Service
public class AuthManager implements IAuthService {
    @Autowired
    private UserRepo userRepo;

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
        user.setPassword(request.getPassword());

        User savedUser = userRepo.save(user);
        BeanUtils.copyProperties(savedUser,dtoUser);
        return dtoUser;
    }
}
