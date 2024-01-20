package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    //根据菜品和套餐中间表返回套餐的id值
    List<Long> getSetmealByDish(List<Long> ids);

    //新增套餐和菜品之间的关系
    void addSetmealDish(List<SetmealDish> setmealDish);
}
