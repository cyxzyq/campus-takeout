package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

    //修改分类
    void updateCategory(Category category);

    //分类分页查询
    Page<Category> findByPageCategory(CategoryPageQueryDTO categoryPageQueryDTO);
}
