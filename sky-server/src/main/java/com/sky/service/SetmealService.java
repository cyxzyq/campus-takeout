package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;

public interface SetmealService {
    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    void addSetmeal(SetmealDTO setmealDTO);

    void statusSetmeal(Long id, Integer status);
}
