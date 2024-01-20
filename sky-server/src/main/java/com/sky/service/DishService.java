package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    void addDish(DishDTO dishDTO);

    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    void delectDish(List<Long> ids);

    void statusDish(Integer status,Long id);

    void updateDish(DishVO dishVO);

    DishVO findByIdDish(Long id);

    List<Dish> findByCategoryIdDish(Long categoryId);
}
