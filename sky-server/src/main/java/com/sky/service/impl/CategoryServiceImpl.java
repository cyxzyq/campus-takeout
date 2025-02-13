package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.interceptor.JwtTokenAdminInterceptor;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

//分类业务层
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    DishMapper dishMapper;
    @Autowired
    SetmealMapper setmealMapper;

    //分类分页查询逻辑
    @Override
    public PageResult findByPageCategory(CategoryPageQueryDTO categoryPageQueryDTO) {
        //使用插件PageHelper进行分页查询的逻辑处理
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        Page<Category> page=categoryMapper.findByPageCategory(categoryPageQueryDTO);
        PageResult pageResult=new PageResult(page.getTotal(),page.getResult());
        return pageResult;
    }

    //启用禁用分类
    @Override
    public void statusCategory(Integer status,Long id) {
        Category category = new Category();
        category.setId(id);
        category.setStatus(status);
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(JwtTokenAdminInterceptor.threadLocal.get());
        categoryMapper.statusCategory(category);
    }

    //新增分类
    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        //将categoryDTO拷贝到category中
        BeanUtils.copyProperties(categoryDTO,category);
        //为category的createTime赋值
        category.setCreateTime(LocalDateTime.now());
        //为category的createUser赋值
        category.setCreateUser(JwtTokenAdminInterceptor.threadLocal.get());
        //status默认为禁用
        category.setStatus(StatusConstant.DISABLE);
        category.setUpdateUser(JwtTokenAdminInterceptor.threadLocal.get());
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.addCategory(category);
    }

    //根据id删除分类
    @Override
    public void delectCategory(Long id) {
        List<Dish> dish = dishMapper.findByCategoryId(id);
        List<Setmeal> setmeal=setmealMapper.findBycategoryIdSetmeal(id);
        //判断该分类是否关联了菜品
        if(dish!=null&& dish.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        //判断该分类是否关联了套餐
        if(setmeal!=null&& setmeal.size()>0){
            throw  new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        //删除分类
        categoryMapper.delect(id);
    }

    //根据分类类型查询
    @Override
    public List<Category> list(Integer type) {
        return categoryMapper.list(type);
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
