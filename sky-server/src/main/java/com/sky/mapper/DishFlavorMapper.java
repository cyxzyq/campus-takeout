package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
//菜品口味操作层
public interface DishFlavorMapper {

    //新增菜品口味数据
    void addDishFlavor(List<DishFlavor> dishFlavor);
}
