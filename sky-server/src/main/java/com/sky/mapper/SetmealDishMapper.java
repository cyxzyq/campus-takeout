package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    //根据菜品和套餐中间表返回菜品的id值
    List<Long> getSetmealByDish(List<Long> ids);
}
