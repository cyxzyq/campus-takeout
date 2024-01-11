package com.sky.mapper;

import com.sky.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    //根据分类id查询套餐
    @Select("select * from setmeal where category_id=#{categoryId}")
    List<Setmeal> findByIdSetmeal(@Param("categoryId") Long id);
}
