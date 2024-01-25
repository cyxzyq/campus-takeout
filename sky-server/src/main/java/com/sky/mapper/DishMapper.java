package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface DishMapper {

    //根据分类id查询菜品
    @Select("select * from Dish where category_id=#{id}")
    List<Dish> findByCategoryId(Long id);

    //新增菜品
    void addDish(Dish dish);

    //菜品分页查询
    Page<DishVO> page(DishPageQueryDTO dishPageQueryDTO);

    //根据id删除菜品
    void delectDish(List<Long> ids);

    //根据id查询菜品
    @Select("select * from dish where id=#{id}")
    Dish findByIdDish(Long id);

    //菜品的起售、停售
    void statusDish(@Param("status") Integer status ,
                    @Param("id") Long id,
                    @Param("updateUser") Long updateUser,
                    @Param("updateTime") LocalDateTime updateTime);

    //修改菜品
    void updateDish(Dish dish);

    //根据分类id查询菜品及口味信息
    List<DishVO> dishList(Long categoryId);
}
