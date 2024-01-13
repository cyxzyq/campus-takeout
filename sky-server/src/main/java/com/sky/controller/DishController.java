package com.sky.controller;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//菜品请求层
@RestController
@Slf4j
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    DishService dishService;

    //新增菜品
    @PostMapping
    public Result addDish(@RequestBody DishDTO dishDTO){
         log.info("新增菜品:{}",dishDTO);
         dishService.addDish(dishDTO);
        return Result.success();
    }

    //菜品分页查询
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询：{}",dishPageQueryDTO);
        PageResult pageResult=dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }
}
