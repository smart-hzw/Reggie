package com.hzw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzw.mapper.OrderDetailMapper;
import com.hzw.mapper.OrderMapper;
import com.hzw.pojo.OrderDetail;
import com.hzw.pojo.Orders;
import com.hzw.service.OrderDetailService;
import com.hzw.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
