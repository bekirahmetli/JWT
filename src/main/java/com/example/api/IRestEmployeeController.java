package com.example.api;

import com.example.dto.DtoEmployee;
import jakarta.validation.constraints.Positive;

/**
 * Employee ile ilgili REST endpointlerini tanımlayan controller arayüzüdür.
 * Bu arayüzü implemente eden controller sınıfı HTTP isteklerini karşılar ve
 * servis katmanına yönlendirir.
 */
public interface IRestEmployeeController {
    /**
     * Verilen ID değerine göre bir çalışanı getirir.
     *
     * @param id Çalışanın veritabanındaki benzersiz kimliği
     * @return Çalışan bilgilerini taşıyan DtoEmployee nesnesi
     */
    public DtoEmployee findEmployeeById(@Positive Long id);
}
