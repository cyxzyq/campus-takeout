package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    //根据菜品和套餐中间表返回套餐的id值
    List<Long> getSetmealByDish(List<Long> ids);

    //新增套餐和菜品之间的关系
    void addSetmealDish(List<SetmealDish> setmealDish);

    //根据套餐id和status=0查询菜品
    @Select("select dish.* from setmeal_dish LEFT JOIN dish ON setmeal_dish.dish_id = dish.id where setmeal_id=#{id} and status=0")
    List<Dish> finddishBysetmealId(Long id);
}
