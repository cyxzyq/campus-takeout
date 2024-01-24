package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

//用户端
@Mapper
public interface UserMapper {

    //根据用户唯一标识符openid查询用户
    @Select("select * from user where openid=#{openid}")
    User findUserByOpenid(String openid);

    //新增用户数据
    void addUser(User user);

}
