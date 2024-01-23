package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//套餐请求层
@RestController
@Slf4j
@RequestMapping("/admin/setmeal")
public class SetmealController {
    @Autowired
    SetmealService setmealService;

    //套餐分页查询
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("员工分页查询：{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    //新建套餐
    @PostMapping
    public Result addSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("新建菜品:{}", setmealDTO);
        setmealService.addSetmeal(setmealDTO);
        return Result.success();
    }

    //套餐的起售、停售
    @PostMapping("/status/{status}")
    public Result statusSetmeal(@PathVariable Integer status, Long id) {
        log.info("套餐的id:{},起售、停售:{}", id, status);
        setmealService.statusSetmeal(id,status);
        return Result.success();
    }

    //根据套餐id查询套餐
    @GetMapping("/{id}")
    public Result<SetmealVO> findSetmealById(@PathVariable Long id){
        log.info("套餐id：{}",id);
        SetmealVO setmealVO=setmealService.findSetmealById(id);
        return  Result.success(setmealVO);
    }

    //修改套餐
    @PutMapping
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐：{}",setmealDTO);
        setmealService.updateSetmeal(setmealDTO);
        return Result.success();
    }

    //批量删除套餐
    @DeleteMapping
    public Result delect(@RequestParam List<Long> ids){
           log.info("批量删除套餐ids:{}",ids);
           setmealService.delect(ids);
           return Result.success();
    }
}
