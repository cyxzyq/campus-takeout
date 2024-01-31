package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

//购物车
@Mapper
public interface ShoppingCartMapper{

    //查询购物车表
    List<ShoppingCart> findShoppingCart(ShoppingCart shoppingCart);

    //更新购物车
    void update(ShoppingCart shoppingCart);

    //添加购物车
    void add(ShoppingCart shoppingCart);

    //清空购物车
    void clean(ShoppingCart shoppingCart);

    //添加多条购物车数据
    void addAll(List<ShoppingCart> shoppingCarts);
}
