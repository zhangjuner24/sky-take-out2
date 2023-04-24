package com.sky.service;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import org.springframework.stereotype.Service;

public interface EmployeeService {


    Employee login(EmployeeLoginDTO employeeLoginDTO);
}
