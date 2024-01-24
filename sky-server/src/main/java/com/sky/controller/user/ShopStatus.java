package com.sky.controller.user;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

//营业状态
@RestController("userShopStatus")
@Slf4j
@RequestMapping("/user/shop")
public class ShopStatus {

    public static final String KEYS = "shopStatus";

    @Autowired
    RedisTemplate redisTemplate;

    //查询营业状态
    @GetMapping("/status")
    public Result<Integer> getShop() {
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get(KEYS);
        log.info("营业状态：{}",shopStatus);
        return Result.success(shopStatus);
    }
}
