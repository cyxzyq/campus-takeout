package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface CategoryMapper {

    //修改分类
    void updateCategory(Category category);

    //分类分页查询
    Page<Category> findByPageCategory(CategoryPageQueryDTO categoryPageQueryDTO);

    //启用，禁用分类
    void statusCategory(Category category);

    //新增分类
    void addCategory(Category category);

    //根据id删除分类
    @Delete("delete from category where id=#{id}")
    void delect(Long id);

    //根据分类类型查询
    @Select("select * from category where type=#{type} order by sort,update_time desc")
    List<Category> list(Integer type);

    //根据菜品id查询菜品名称
    @Select("select name from category where id=#{categoryId}")
    String findByCategoryIdCategoryName(Long categoryId);
}
