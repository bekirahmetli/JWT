package com.example.business.concretes;

import com.example.business.abstracts.IEmployeeService;
import com.example.dao.EmployeeRepo;
import com.example.dto.DtoDepartment;
import com.example.dto.DtoEmployee;
import com.example.entities.Department;
import com.example.entities.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Employee ile ilgili iş mantığını yöneten servis sınıfıdır.
 * IEmployeeService arayüzünü implemente ederek çalışanlara ait verilerin
 * işlenmesi, dönüştürülmesi ve dışarıya DTO olarak verilmesini sağlar.
 */
@Service
public class EmployeeManager implements IEmployeeService {
    @Autowired
    private EmployeeRepo employeeRepo;

    /**
     * Verilen ID'ye göre bir çalışanı bulur ve DtoEmployee formatında döner.
     * Çalışana ait Department bilgisi de ayrıca DtoDepartment olarak dönüştürülür.
     *
     * @param id Çalışanın veritabanındaki benzersiz kimliği
     * @return Bulunan çalışanın DTO formatındaki hali. Bulunamazsa null döner.
     */
    @Override
    public DtoEmployee findEmployeeById(Long id) {
        DtoEmployee dtoEmployee = new DtoEmployee();
        DtoDepartment dtoDepartment = new DtoDepartment();
        Optional<Employee> optional = employeeRepo.findById(id);
        if(optional.isEmpty()){
            return null;
        }
        Employee employee = optional.get();
        Department department = employee.getDepartment();

        BeanUtils.copyProperties(employee,dtoEmployee);
        BeanUtils.copyProperties(department,dtoDepartment);

        dtoEmployee.setDepartment(dtoDepartment);
        return dtoEmployee;
    }
}
