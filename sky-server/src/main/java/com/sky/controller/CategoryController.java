package com.sky.controller;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    //修改分类
    @PutMapping
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类：{}",categoryDTO);
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }

    //启用禁用分类
    @PostMapping("/status/{status}")
    public Result statusCategory(@PathVariable Integer status,Long id){
        log.info("status：{}，id：{}",status,id);
        categoryService.statusCategory(status,id);
        return Result.success();
    }

    //新增分类
    @PostMapping()
    public Result addCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类：{}", categoryDTO);
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }
}
