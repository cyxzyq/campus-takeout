package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.naming.Name;
import java.time.LocalDateTime;
import java.util.List;

//套餐数据修改层
@Mapper
public interface SetmealMapper {

    //根据分类id查询套餐
    List<Setmeal> findBycategoryIdSetmeal(Long id);

    //停售和菜品关联的套餐
    void findByIdSetmeal(@Param("status") Integer status ,
                         @Param("setmealIdList") List<Long> setmealIdList,
                         @Param("updateUser") Long updateUser,
                         @Param("updateTime") LocalDateTime updateTime);

    //套餐分页查询
    Page<SetmealVO> page(SetmealPageQueryDTO setmealPageQueryDTO);

    //新建菜品
    void addSetmeal(Setmeal setmeal);

    //起售、停售套餐
    @Select("update setmeal set status=#{status} where id=#{id}")
    void statusSetmeal(@Param("id") Long id, @Param("status") Integer status);

    //根据套餐id查询套餐和分类名称
    @Select("select category.name as categoryName,setmeal.*" +
            "from setmeal left join category on setmeal.category_id=category.id " +
            "where setmeal.id=#{id}")
    SetmealVO findSetmealById(Long id);

    //修改套餐
    void updateSetmeal(Setmeal setmeal);

    //根据多个套餐id查询多个套餐信息
    List<Setmeal> getSetmeal(List<Long> ids);

    //批量删除套餐
    void delect(List<Long> ids);
}
