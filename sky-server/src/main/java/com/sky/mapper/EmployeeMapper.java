package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    //新增员工
    @Select("insert into employee(id,username,name,password,phone,sex,id_number,status,create_time,update_time,create_user,update_user) " +
            "values(null,#{username},#{name},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void addEmployee(Employee employee);

    //条件查询员工信息
    Page<Employee> findByEmployee(EmployeePageQueryDTO employeePageQueryDTO);

    //启用，禁用员工账号
    @Select("update employee set status = #{status} where id=#{id}")
    void statusEmployee(@Param("status") Integer status,@Param("id") Long id);

    //根据id查询员工
    @Select("select * from employee where id=#{id}")
    Employee getByIdEmployee(Long id);

    //修改员工信息
    void updateEmployee(Employee employee);

    //修改密码
    void updatePassword(PasswordEditDTO passwordEditDTO);

    //查询当前用户密码
    @Select("select password from employee where id=#{empId}")
    String findPassword(Long empId);
}
