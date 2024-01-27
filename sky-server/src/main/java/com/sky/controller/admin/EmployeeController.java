package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);




        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    //新增员工
    @PostMapping
    public Result addEmployee(@RequestBody EmployeeDTO employeeDTO){
            log.info("新增员工：{}", employeeDTO);
            employeeService.addEmployee(employeeDTO);
            return Result.success();
    }

    //员工分页查询
    @GetMapping("/page")
    public Result<PageResult> findByEmployee(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("查询条件：{}" ,employeePageQueryDTO);
        PageResult pageResult = employeeService.findByEmployee(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    //启用禁用员工账号
    @PostMapping("/status/{status}")
    public Result statusEmployee(@PathVariable Integer status,Long id){
          log.info("status:{},id:{}",status,id);
          employeeService.statusEmployee(status,id);
          return Result.success();
    }

    //根据id查询员工
    @GetMapping("/{id}")
    public Result<Employee> getByIdEmployee(@PathVariable Long id){
        log.info("查询的员工id:{}",id);
        Employee employee=employeeService.getByIdEmployee(id);
        return Result.success(employee);
    }

    //编辑员工信息
    @PutMapping
    public Result updateEmployee(@RequestBody EmployeeDTO employeeDTO){
        log.info("员工信息更新内容：{}",employeeDTO);
        employeeService.updateEmployee(employeeDTO);
        return Result.success();
    }
    //修改密码
    @PutMapping("/editPassword")
    public Result updatePassword(@RequestBody PasswordEditDTO passwordEditDTO){
        log.info("修改密码：{}",passwordEditDTO);
        employeeService.updatePassword(passwordEditDTO);
        return Result.success();
    }
}
