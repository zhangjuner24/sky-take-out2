package com.sky.web.admin;

import cn.hutool.jwt.JWTUtil;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    //员工登陆
    @PostMapping("/login")
    public Result login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {

        //调用service登陆
        Employee employee = employeeService.login(employeeLoginDTO);

        //制作token
        Map<String, Object> claims = new HashMap<>();
        claims.put("empId", employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecret(),
                jwtProperties.getAdminTtl(),
                claims);
        //返回vo给前端
        EmployeeLoginVO builder = EmployeeLoginVO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .token(token)
                .userName(employee.getUsername())
                .build();
        return Result.success(builder);

    }@PostMapping("/logout")
    public Result logout(){
        return Result.success();
    }
}
