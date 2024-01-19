package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
//菜品口味操作层
public interface DishFlavorMapper {

    //新增菜品口味数据
    void addDishFlavor(List<DishFlavor> dishFlavor);

    //删除菜品关联的口味信息
    void delectDishFlavor(List<Long> id);

    //根据菜品id查询口味信息
    @Select("select * from dish_flavor where dish_id=#{id}")
    List<DishFlavor> findBydishId(Long id);
}
