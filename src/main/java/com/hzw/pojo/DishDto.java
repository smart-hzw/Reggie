package com.hzw.pojo;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;


/**
 * DTO:全称为 Data Transfer Object,即数据传输对象，一般用于展示层与服务层之间的数据传输
 */
@Data
public class DishDto extends Dish {
    //菜品对应的口味数据
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
