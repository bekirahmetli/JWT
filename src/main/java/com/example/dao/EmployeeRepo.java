package com.example.dao;

import com.example.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Employee varlıkları (entity) için CRUD işlemlerini gerçekleştiren
 * JPA Repository arayüzüdür.
 *
 * JpaRepository arayüzünü genişleterek, çalışan (Employee) nesneleri için
 * hazır veri erişim metodlarını sağlar.
 */
@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Long> {
}
