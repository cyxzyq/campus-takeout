package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    //根据分类id查询菜品
    @Select("select * from Dish where category_id=#{id}")
    List<Dish> findByIdDish(Long id);

    //新增菜品
    void addDish(Dish dish);

    //菜品分页查询
    Page<DishVO> page(DishPageQueryDTO dishPageQueryDTO);
}
