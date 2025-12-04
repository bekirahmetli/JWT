package com.example.dao;

import com.example.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * RefreshToken varlıkları için veri erişim işlemlerini yöneten
 * JPA repository arayüzüdür.
 *
 * Bu arayüz, Spring Data JPA tarafından otomatik olarak implement edilir
 * ve CRUD işlemleri hazır hale getirilir.
 */
@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
