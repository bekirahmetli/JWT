package com.example.business.abstracts;

import com.example.dto.DtoEmployee;

/**
 * Employee ile ilgili iş mantığını yöneten servis arayüzüdür.
 * Bu arayüz, çalışan verilerine erişmek veya işlem yapmak için
 * servis katmanında uygulanması gereken metotları tanımlar.
 */
public interface IEmployeeService {
    /**
     * Verilen ID değerine göre bir çalışan arar.
     *
     * @param id Çalışanın veritabanındaki benzersiz kimliği
     * @return Çalışan bilgilerini taşıyan DtoEmployee nesnesi
     */
    DtoEmployee findEmployeeById(Long id);
}
