package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetmealDishVO implements Serializable {
            
    //份数
    private Integer copies;

    //菜品描述
    private String description;

    //图片路径
    private String image;

    //菜品名称
    private String name;
}
