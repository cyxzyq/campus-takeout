package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

//套餐数据修改层
@Mapper
public interface SetmealMapper {

    //根据分类id查询套餐
    @Select("select * from setmeal where category_id=#{id}")
    List<Setmeal> findBycategoryIdSetmeal(Long id);

    //停售和菜品关联的套餐
    void findByIdSetmeal(@Param("status") Integer status ,
                         @Param("setmealIdList") List<Long> setmealIdList,
                         @Param("updateUser") Long updateUser,
                         @Param("updateTime") LocalDateTime updateTime);

    //套餐分页查询
    Page<SetmealVO> page(SetmealPageQueryDTO setmealPageQueryDTO);
}
