package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.entity.SetmealDish;
import com.sky.vo.SetmealDishVO;
import org.apache.ibatis.annotations.Delete;
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

    //根据套餐id查询套餐关联菜品信息
    @Select("select * from setmeal_dish where setmeal_id=#{id}")
    List<SetmealDish> findsetmealDishesById(Long id);

    //删除套餐关联的菜品
    @Delete("delete from setmeal_dish where setmeal_id=#{setmealId}")
    void delectSetmealDish(Long setmealId);

    //批量删除套餐关联的套餐和菜品的中间表的数据
    void delect(List<Long> ids);

    //根据套餐id查询套餐关联的菜品和套餐和菜品的中间表
    List<SetmealDishVO> findsetmealDishandDishBysetmealId(Long id);
}
