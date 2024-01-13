package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.interceptor.JwtTokenAdminInterceptor;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

//菜品业务处理层
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorMapper dishFlavorMapper;
    @Autowired
    SetmealDishMapper setmealDishMapper;

    //新增菜品
    @Transactional
    @Override
    public void addDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        //将DishDTO的数据复制到Dish中
        BeanUtils.copyProperties(dishDTO, dish);
        //对Dish其他数据进行赋值
        dish.setCreateTime(LocalDateTime.now());
        dish.setCreateUser(JwtTokenAdminInterceptor.threadLocal.get());
        dish.setUpdateTime(LocalDateTime.now());
        dish.setUpdateUser(JwtTokenAdminInterceptor.threadLocal.get());
        //菜品状态默认停售
        dish.setStatus(0);
        //对菜品数据操作
        dishMapper.addDish(dish);

        List<DishFlavor> dishFlavor = dishDTO.getFlavors();
        //判断菜品的口味是否为空
        if (dishFlavor != null && dishFlavor.size() != 0) {
            //遍历dishFlavor集合，将菜品返回的主键值给DishFlavor
            dishFlavor.forEach(dishFlavor1 -> dishFlavor1.setDishId(dish.getId()));
            dishFlavorMapper.addDishFlavor(dishFlavor);
        }
    }

    //菜品分页查询
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        //PageHelper分页查询
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.page(dishPageQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }

    //菜品批量删除
    @Transactional
    @Override
    public void delectDish(List<Long> ids) {
        //查询菜品并判断status的值
        for (Long id : ids) {
            Dish dish = dishMapper.findByIdDish(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //查询菜品和套餐的中间表，判断菜品是否与套餐关联
        List<Long> longList=setmealDishMapper.getSetmealByDish(ids);
        if(longList!=null && longList.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
            //删除菜品
            dishMapper.delectDish(ids);
            //删除菜品关联的口味
            dishFlavorMapper.delectDishFlavor(ids);
    }
}
