package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void addEmployee(EmployeeDTO employeeDTO);

    PageResult findByEmployee(EmployeePageQueryDTO employeePageQueryDTO);

    void statusEmployee(Integer status, Long id);

    Employee getByIdEmployee(Long id);

    void updateEmployee(EmployeeDTO employeeDTO);

    void updatePassword(PasswordEditDTO passwordEditDTO);
}
