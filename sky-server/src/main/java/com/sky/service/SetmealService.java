package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.SetmealDishVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    void addSetmeal(SetmealDTO setmealDTO);

    void statusSetmeal(Long id, Integer status);

    SetmealVO findSetmealById(Long id);

    void updateSetmeal(SetmealDTO setmealDTO);

    void delect(List<Long> ids);

    List<Setmeal> findSetmealByCategoryId(Long categoryId);

    List<SetmealDishVO> findDishDiBySetmealId(Long id);
}
