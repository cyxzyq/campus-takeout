package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
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

    @Select("insert into employee(id,username,name,password,phone,sex,id_number,status,create_time,update_time,create_user,update_user) " +
            "values(null,#{username},#{name},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void addEmployee(Employee employee);

    Page<Employee> findByEmployee(EmployeePageQueryDTO employeePageQueryDTO);

    @Select("update employee set status = #{status} where id=#{id}")
    void statusEmployee(Integer status, Integer id);
}
