package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//购物车
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    //添加购物车
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加购物车：{}",shoppingCartDTO);
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }

    //查看购物车
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        List<ShoppingCart> shoppingCartList=shoppingCartService.list();
        return Result.success(shoppingCartList);
    }

    //清空购物车
    @DeleteMapping("/clean")
    public Result clean(){
        shoppingCartService.clean();
        return Result.success();
    }

    //删除购物车其中一个商品
    @PostMapping("/sub")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("删除购物车中指定的菜品：{}",shoppingCartDTO);
        shoppingCartService.sub(shoppingCartDTO);
        return Result.success();
    }
}
