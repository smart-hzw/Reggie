package com.hzw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzw.pojo.Setmeal;
import com.hzw.pojo.SetmealDto;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    //新增套餐，同时保存套餐和菜品的关联关系
    public void saveWithDish(SetmealDto setmealDto);

    public void deleteWithDish(@RequestParam List<Long> ids);
}
