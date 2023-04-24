package com.sky.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.ThreadLocalUtil;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.BusinessException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public PageResult getpage(EmployeePageQueryDTO employeePageQueryDTO) {
        //开启分页
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        //查询list集合
        List list = employeeMapper.getList(employeePageQueryDTO.getName());
        Page page = (Page) list;
        return new PageResult(page.getTotal(),page.getResult());

    }

    @Override
    public void save(EmployeeDTO employeeDTO) {
        // 1.参数校验
        if (
                StrUtil.isBlank(employeeDTO.getUsername()) ||
                        StrUtil.isBlank(employeeDTO.getName()) ||
                        StrUtil.isBlank(employeeDTO.getPhone()) ||
                        StrUtil.isBlank(employeeDTO.getIdNumber())
        ) {
            throw new BusinessException("参数非法");
        }
        // 2.业务校验
        // 2-1 账号唯一
        Employee byUsername = employeeMapper.getusername(employeeDTO.getUsername());
        if (byUsername != null) {
            throw new BusinessException("此账号已存在");
        }
        // 2-2 手机号唯一
        Employee byPhone = employeeMapper.getByPhone(employeeDTO.getPhone());
        if (byPhone != null) {
            throw new BusinessException("此手机号已存在");
        }
        // 2-3 身份证号唯一
        Employee byIdNumber = employeeMapper.getIdNumber(employeeDTO.getIdNumber());
        if (byIdNumber!=null) {
            throw new BusinessException("此身份证号已存在");
        }
        // 3. dto -> entity
        Employee employee = BeanUtil.copyProperties(employeeDTO, Employee.class);
        // 3-1 补全信息
        String md5 = SecureUtil.md5(PasswordConstant.DEFAULT_PASSWORD);
        employee.setPassword(md5);
        employee.setStatus(StatusConstant.ENABLE);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        employee.setCreateUser(ThreadLocalUtil.getCurrentId());
        employee.setUpdateUser(ThreadLocalUtil.getCurrentId());

        // 4.调用mapper保存到数据库
        employeeMapper.insert(employee);
    }

}
