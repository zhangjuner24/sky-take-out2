package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;

public interface EmployeeService {


    Employee login(EmployeeLoginDTO employeeLoginDTO);


    PageResult getpage(EmployeePageQueryDTO employeePageQueryDTO);

    void save(EmployeeDTO employeeDTO);

    Employee getById(Long id);

    // 修改员工
    void updateById(EmployeeDTO employeeDTO);

    // 启动禁用
    void startOrStop(Integer status, Long id);



}
