package com.hzw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzw.pojo.Category;
import com.hzw.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish>{

}
