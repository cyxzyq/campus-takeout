package com.sky.controller.admin;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

//营业状态
@RestController("adminShopStatus")
@Slf4j
@RequestMapping("/admin/shop")
public class ShopStatus {

    public  static final String KEYS="shopStatus";

    @Autowired
    RedisTemplate redisTemplate;

    //查询营业状态
    @GetMapping("/status")
    public Result<Integer> getShop(){
        Integer shopStatus =(Integer) redisTemplate.opsForValue().get(KEYS);
        return Result.success(shopStatus);
    }

    //设置营业状态
    @PutMapping("/{status}")
    public Result updateShopStatus(@PathVariable Integer status){
        log.info("shopStatus:{}",status);
        redisTemplate.opsForValue().set(KEYS,status);
        return Result.success();
    }
}
