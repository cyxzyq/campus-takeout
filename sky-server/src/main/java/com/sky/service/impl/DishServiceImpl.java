package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.interceptor.JwtTokenAdminInterceptor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
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

    //新增菜品
    @Transactional
    @Override
    public void addDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        //将DishDTO的数据复制到Dish中
        BeanUtils.copyProperties(dishDTO,dish);
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
        if(dishFlavor!=null && dishFlavor.size()!=0){
            //遍历dishFlavor集合，将菜品返回的主键值给DishFlavor
            dishFlavor.forEach(dishFlavor1 -> dishFlavor1.setDishId(dish.getId()));
            dishFlavorMapper.addDishFlavor(dishFlavor);
        }
    }
}
