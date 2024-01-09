package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.interceptor.JwtTokenAdminInterceptor;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

//分类业务层
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;


    //分类分页查询逻辑
    @Override
    public PageResult findByPageCategory(CategoryPageQueryDTO categoryPageQueryDTO) {
        //使用插件PageHelper进行分页查询的逻辑处理
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        Page<Category> page=categoryMapper.findByPageCategory(categoryPageQueryDTO);
        PageResult pageResult=new PageResult(page.getTotal(),page.getResult());
        return pageResult;
    }

    //修改分类
    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        Category category=new Category();
        //categoryDTO复制到category对象中去
        BeanUtils.copyProperties(categoryDTO,category);
        //修改更新时间
        category.setUpdateTime(LocalDateTime.now());
        //记录修改人的id
        category.setUpdateUser(JwtTokenAdminInterceptor.threadLocal.get());
        categoryMapper.updateCategory(category);
    }
}
