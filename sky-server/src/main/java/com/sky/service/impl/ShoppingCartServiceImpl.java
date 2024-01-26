package com.sky.service.impl;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.interceptor.JwtTokenUserInterceptor;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//购物车
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    //添加购物车
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        //获取当前用户id
        shoppingCart.setUserId(JwtTokenUserInterceptor.threadLocal.get());
        //获取加入的商品是否在购物车内
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.findShoppingCart(shoppingCart);
        //若在购物车内，则购物车内的商品数量+1
        if (shoppingCartList != null && shoppingCartList.size() > 0) {
            //对购物车表对应的商品数量+1
            shoppingCartList.get(0).setNumber(shoppingCartList.get(0).getNumber() + 1);
            shoppingCartMapper.update(shoppingCartList.get(0));
            return;
        }
        //若没有，则添加购物车
        if (shoppingCart.getDishId() != null) {
            //查询菜品名称，价格，图片
            Dish dish = dishMapper.findByIdDish(shoppingCart.getDishId());
            shoppingCart.setAmount(dish.getPrice());
            shoppingCart.setImage(dish.getImage());
            shoppingCart.setName(dish.getName());
        } else {
            //查询套餐名称，价格，图片
            List<Long> list = new ArrayList<>();
            list.add(shoppingCart.getSetmealId());
            List<Setmeal> setmeal = setmealMapper.getSetmeal(list);
            shoppingCart.setImage(setmeal.get(0).getImage());
            shoppingCart.setName(setmeal.get(0).getName());
            shoppingCart.setAmount(setmeal.get(0).getPrice());
        }
        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCartMapper.add(shoppingCart);
    }

    //查看购物车
    @Override
    public List<ShoppingCart> list() {
       return shoppingCartMapper.list();
    }
}
