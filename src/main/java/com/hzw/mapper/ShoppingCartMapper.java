package com.hzw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzw.pojo.Dish;
import com.hzw.pojo.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart>{

}
