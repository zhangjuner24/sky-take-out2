package com.sky.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.exception.BusinessException;
import com.sky.mapper.EmployeeMapper;
import com.sky.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

        //员工登陆
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        //参数检验一下,
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            throw new BusinessException("非法参数");
        }
        //根据用户名查询数据库
        Employee employee = employeeMapper.getusername(username);
        //业务校验
        //第一个查一下用户是不是存在,去数据库查
        if (employee==null) {
            throw new BusinessException("用户不存在,你应该是输入错误了");
        }
        //查一下密码是否正确,
        String s = SecureUtil.md5(password);//因为密码是加密的.所以需要转换
        if (!StrUtil.equals(s,employee.getPassword())) {
            throw new BusinessException("你的密码错误,请您重新输入");
        }
        //看一下账号是否被禁用了
        if (employee.getStatus() == StatusConstant.DISABLE) {
            throw new BusinessException("此账号被禁用了,请联系后台管理员阿");
        }
        return employee;

    }
}
