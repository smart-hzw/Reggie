package com.hzw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzw.pojo.Category;
import com.hzw.pojo.Dish;
import com.hzw.pojo.DishDto;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface DishService extends IService<Dish> {
    //新增菜品，同时插入菜品对应的口味数据，需要操作两张表：dish，dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlayor(DishDto dishDto);

    //根据id删除菜品信息和对应的口味信息
    public void deleteWithFlavor(@RequestParam List<Long> ids);
}
