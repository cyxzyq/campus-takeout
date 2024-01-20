package com.sky.controller;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//套餐请求层
@RestController
@Slf4j
@RequestMapping("/admin/setmeal")
public class SetmealController {
    @Autowired
    SetmealService setmealService;

    //套餐分页查询
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("员工分页查询：{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    //新建套餐
    @PostMapping
    public Result addSetmeal(@RequestBody SetmealDTO setmealDTO){
        log.info("新建菜品:{}",setmealDTO);
        setmealService.addSetmeal(setmealDTO);
        return Result.success();
    }
}
