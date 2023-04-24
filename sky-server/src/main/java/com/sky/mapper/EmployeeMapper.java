package com.sky.mapper;

import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    //条件查询
    @Select("select * from employee where username=#{username}")
    Employee getusername(String username);

    List getList(String name);

    //查询电话
    @Select("select * from employee where phone=#{phone}")
    Employee getByPhone(String phone);
    //查一下
    @Select("select * from employee where id_number=#{idNumber}")
    Employee getIdNumber(String idNumber);
    //保存
    void insert(Employee employee);
}
