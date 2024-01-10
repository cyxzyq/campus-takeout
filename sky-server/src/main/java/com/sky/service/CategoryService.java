package com.sky.service;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;

public interface CategoryService {
    PageResult findByPageCategory(CategoryPageQueryDTO categoryPageQueryDTO);

    void updateCategory(CategoryDTO categoryDTO);

    void statusCategory(Integer status, Long id);

    void addCategory(CategoryDTO categoryDTO);

    void delectCategory(Long id);
}
