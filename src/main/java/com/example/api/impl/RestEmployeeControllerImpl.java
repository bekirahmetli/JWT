package com.example.api.impl;

import com.example.api.IRestEmployeeController;
import com.example.business.abstracts.IEmployeeService;
import com.example.dto.DtoEmployee;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Employee ile ilgili REST endpointlerini yöneten controller sınıfıdır.
 * IRestEmployeeController arayüzünü implemente eder.
 * HTTP isteklerini alır ve servis katmanına yönlendirir.
 */
@RestController
@RequestMapping("/employees")
public class RestEmployeeControllerImpl implements IRestEmployeeController {
    @Autowired
    private IEmployeeService employeeService;

    /**
     * Verilen ID değerine göre bir çalışanı getirir.
     *
     * @param id Çalışanın veritabanındaki benzersiz kimliği
     * @return DtoEmployee nesnesi, çalışan bilgilerini içerir
     */
    @GetMapping("/{id}")
    @Override
    public DtoEmployee findEmployeeById(@Valid @NotEmpty @PathVariable("id") Long id) {
        return employeeService.findEmployeeById(id);
    }
}
