package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.interceptor.JwtTokenAdminInterceptor;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

//套餐逻辑处理层
@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    SetmealDishMapper setmealDishMapper;

    //套餐分页查询
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        //使用pageHelper插件
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.page(setmealPageQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }

    //新建菜品
    @Transactional
    @Override
    public void addSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmeal.setCreateUser(JwtTokenAdminInterceptor.threadLocal.get());
        setmeal.setCreateTime(LocalDateTime.now());
        setmeal.setUpdateTime(LocalDateTime.now());
        setmeal.setUpdateUser(JwtTokenAdminInterceptor.threadLocal.get());
        //创建默认为停售状态
        setmeal.setStatus(StatusConstant.DISABLE);
        //操作数据库新增菜品
        setmealMapper.addSetmeal(setmeal);
        //获取套餐相关菜品信息
        List<SetmealDish> setmealDish=setmealDTO.getSetmealDishes();
        //套餐主键返回
        for (SetmealDish dish : setmealDish) {
            dish.setSetmealId(setmeal.getId());
        }
        //操作套餐菜品关系表
        setmealDishMapper.addSetmealDish(setmealDish);
    }

    //套餐的起售、停售
    @Override
    public void statusSetmeal(Long id, Integer status) {
        if(status.equals(StatusConstant.DISABLE)){
            //停售套餐
            setmealMapper.statusSetmeal(id,status);
            return;
        }
        //根据套餐id和status=0查询菜品
        List<Dish> dishList=setmealDishMapper.finddishBysetmealId(id);
        //若查询到的菜品不为空则不允许起售套餐
        if(dishList!=null && dishList.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ENABLE_FAILED);
        }
        //起售套餐
        setmealMapper.statusSetmeal(id,status);
    }
}
