package com.hzw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzw.pojo.Orders;
import com.hzw.pojo.User;

public interface OrderService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);
}
