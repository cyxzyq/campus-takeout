package com.sky.service.impl;

import com.alibaba.druid.sql.visitor.functions.If;
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

    //根据套餐id查询套餐
    @Override
    public SetmealVO findSetmealById(Long id) {
        //根据套餐id查询套餐关联菜品信息
        List<SetmealDish> setmealDishes=setmealDishMapper.findsetmealDishesById(id);
        SetmealVO setmealVO = setmealMapper.findSetmealById(id);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    //修改套餐
    @Transactional
    @Override
    public void updateSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmeal.setUpdateTime(LocalDateTime.now());
        setmeal.setUpdateUser(JwtTokenAdminInterceptor.threadLocal.get());
        //修改套餐表数据
        setmealMapper.updateSetmeal(setmeal);
        //判断套餐是关联菜品
        List<SetmealDish> setmealDishList=setmealDishMapper.findsetmealDishesById(setmealDTO.getId());
        if(setmealDishList!=null && setmealDishList.size()>0){
            //删除套餐关联的菜品
            setmealDishMapper.delectSetmealDish(setmealDTO.getId());
        }
        List<SetmealDish> setmealDish=setmealDTO.getSetmealDishes();
        //setmealId值赋值给setmealDish
        for (SetmealDish dish : setmealDish) {
            dish.setSetmealId(setmealDTO.getId());
        }
        //新增套餐关联菜品信息
        setmealDishMapper.addSetmealDish(setmealDish);
    }

    //批量删除套餐
    @Transactional
    @Override
    public void delect(List<Long> ids) {
        //获取该套餐的信息
        List<Setmeal> setmeal=setmealMapper.getSetmeal(ids);
        //判断该套餐是否为启售状态
        for (Setmeal setmeal1 : setmeal) {
            //若为启售状态抛异常
            if(setmeal1.getStatus().equals(StatusConstant.ENABLE)){
              throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        //套餐表批量删除套餐
        setmealMapper.delect(ids);
        //批量删除套餐关联的套餐和菜品的中间表的数据
        setmealDishMapper.delect(ids);
    }
}
