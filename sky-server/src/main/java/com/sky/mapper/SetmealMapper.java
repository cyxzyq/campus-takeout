package com.sky.mapper;

import com.sky.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SetmealMapper {

    //根据分类id查询套餐
    @Select("select * from setmeal where category_id=#{id}")
    List<Setmeal> findBycategoryIdSetmeal(Long id);

    void findByIdSetmeal(@Param("status") Integer status ,
                         @Param("setmealIdList") List<Long> setmealIdList,
                         @Param("updateUser") Long updateUser,
                         @Param("updateTime") LocalDateTime updateTime);
}
