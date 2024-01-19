package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.interceptor.JwtTokenAdminInterceptor;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

import static com.sky.constant.PasswordConstant.DEFAULT_PASSWORD;
import static com.sky.constant.StatusConstant.ENABLE;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }


        //密码比对
        //对前端传进来的密码进行md5编码
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //将密码存储在threadLocal中
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        //3、返回实体对象
        return employee;
    }

    //新增员工
    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //把EmployeeDTO对象里面数据复制到Employee中
        BeanUtils.copyProperties(employeeDTO, employee);
        //赋值加密后的密码到Employee中
        employee.setPassword(DigestUtils.md5DigestAsHex(DEFAULT_PASSWORD.getBytes()));
        //赋值常量1到Employee中
        employee.setStatus(ENABLE);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //从JwtTokenAdminInterceptor中的threadLocal对象中拿到empId
        employee.setCreateUser(JwtTokenAdminInterceptor.threadLocal.get());
        employee.setUpdateUser(JwtTokenAdminInterceptor.threadLocal.get());
        employeeMapper.addEmployee(employee);
    }

    //员工分页查询
    @Override
    public PageResult findByEmployee(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        Page<Employee> page = employeeMapper.findByEmployee(employeePageQueryDTO);
        PageResult pageResult=new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }

    //启用，禁用员工账号
    @Override
    public void statusEmployee(Integer status, Long id) {
        employeeMapper.statusEmployee(status,id);
    }

    //根据id查询员工
    @Override
    public Employee getByIdEmployee(Long id) {
        Employee employee=employeeMapper.getByIdEmployee(id);
        return employee;
    }

    //编辑员工信息
    @Override
    public void updateEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(JwtTokenAdminInterceptor.threadLocal.get());
        employeeMapper.updateEmployee(employee);
    }

    //修改员工密码
    @Override
    public void updatePassword(PasswordEditDTO passwordEditDTO) {
//           在ThreadLocal里获取empId
           passwordEditDTO.setEmpId(JwtTokenAdminInterceptor.threadLocal.get());
//           在数据库里面查询当前账户的密码
            String password=employeeMapper.findPassword(passwordEditDTO.getEmpId());
//            对用户输入的旧密码进行md5加密
            String oldPassword=DigestUtils.md5DigestAsHex(passwordEditDTO.getOldPassword().getBytes());
           //判断旧密码是否正确
           if(!oldPassword.equals(password)){
               throw new PasswordErrorException(MessageConstant.PASSWORD_EDIT_FAILED);
           }
           //对新密码进行md加密
           passwordEditDTO.setNewPassword(DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes()));
           employeeMapper.updatePassword(passwordEditDTO);
    }

}
