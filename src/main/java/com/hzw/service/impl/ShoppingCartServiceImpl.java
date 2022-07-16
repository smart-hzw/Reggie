package com.hzw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzw.mapper.ShoppingCartMapper;
import com.hzw.mapper.UserMapper;
import com.hzw.pojo.ShoppingCart;
import com.hzw.pojo.User;
import com.hzw.service.ShoppingCartService;
import com.hzw.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
