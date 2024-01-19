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
import java.util.List;

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

    //菜品批量删除
    @DeleteMapping
    public Result delectDish(@RequestParam List<Long> ids){
        log.info("删除菜品的id:{}",ids);
        dishService.delectDish(ids);
        return Result.success();
    }

    //菜品的起售、停售
    @PostMapping("/status/{status}")
    public Result statusDish(@PathVariable Integer status,Long id){
        log.info("菜品的起售、停售：status：{}，id：{}",status,id);
        dishService.statusDish(status,id);
        return Result.success();
    }

    //修改菜品
    @PutMapping
    public Result updateDish(@RequestBody DishVO dishVO){
        log.info("修改菜品:{}",dishVO);
        dishService.updateDish(dishVO);
        return Result.success();
    }

    //根据id查询菜品
    @GetMapping("/{id}")
    public Result<DishVO> findByIdDish(@PathVariable Long id){
        log.info("菜品id：{}",id);
        DishVO dishVO=dishService.findByIdDish(id);
        return Result.success(dishVO);
    }
}
