package com.sky.mapper;

import com.sky.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.poi.ss.formula.functions.Count;

import java.util.List;

@Mapper
public interface DishMapper {

    //根据分类id查询菜品
    @Select("select * from Dish where category_id=#{id}")
    List<Dish> findByIdDish(Long id);
}
