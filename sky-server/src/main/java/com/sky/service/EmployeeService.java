package com.sky.service;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;

public interface EmployeeService {


    Employee login(EmployeeLoginDTO employeeLoginDTO);


    PageResult getpage(EmployeePageQueryDTO employeePageQueryDTO);
}
