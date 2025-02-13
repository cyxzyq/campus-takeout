package com.sky.controller.user;


import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.Category;
import com.sky.entity.Setmeal;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealDishVO;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//用户端
@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    JwtProperties jwtProperties;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private DishService dishService;

    //微信登录
    @PostMapping("/user/login")
    public Result<UserLoginVO> userLogin(@RequestBody UserLoginDTO userLoginDTO){
        log.info("微信登录授权码code:{}",userLoginDTO.getCode());
        User user=userService.userLogin(userLoginDTO.getCode());

        Map<String, Object> map = new HashMap<>();
        map.put(JwtClaimsConstant.USER_ID,user.getId());
        //生成用户JWT令牌
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), map);
        UserLoginVO userLoginVO=new UserLoginVO(user.getId(),user.getOpenid(),token);
        return Result.success(userLoginVO);
    }

    //根据类型查询分类
    @GetMapping("/category/list")
    public Result<List> list(Integer type) {
        log.info("分类类型：{}", type);
        List<Category> category = categoryService.list(type);
        return Result.success(category);
    }

    //根据分类id查询套餐
    @Cacheable(cacheNames = "setmealCache",key = "#categoryId")
    @GetMapping("/setmeal/list")
    public Result<List<Setmeal>> list(Long categoryId){
        log.info("分类id：{}",categoryId);
        List<Setmeal> setmealList=setmealService.findSetmealByCategoryId(categoryId);
        return Result.success(setmealList);
    }

    //根据套餐id查询套餐关联的菜品信息
    @GetMapping("/setmeal/dish/{id}")
    public Result<List<SetmealDishVO>> findSetmealById(@PathVariable Long id){
        log.info("套餐id：{}",id);
        List<SetmealDishVO> setmealDishVO=setmealService.findDishDiBySetmealId(id);
        return  Result.success(setmealDishVO);
    }

    //根据分类id查询菜品及口味信息
    @Cacheable(cacheNames = "dishCache",key = "#categoryId")
    @GetMapping("/dish/list")
    public Result<List<DishVO>> dishList(Long categoryId){
        log.info("分类id:{}",categoryId);
        List<DishVO> dishVOList=dishService.dishList(categoryId);
        return Result.success(dishVOList);
    }
}
