package com.sky.controller;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//分类管理
@RestController
@Slf4j
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    //分类分页查询
    @GetMapping("/page")
    public Result<PageResult> finByPageCategory(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分类基本信息：{}",categoryPageQueryDTO);
        PageResult pageResult=categoryService.findByPageCategory(categoryPageQueryDTO);
        return Result.success(pageResult);
    }
}
