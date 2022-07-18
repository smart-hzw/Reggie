package com.hzw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzw.common.BaseContext;
import com.hzw.common.R;
import com.hzw.pojo.*;
import com.hzw.service.OrderDetailService;
import com.hzw.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderDetailService orderDetailService;
    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders.toString());
        orderService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * 获取管理端订单明细
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, Long number, String beginTime, String endTime) throws ParseException {
        log.info("page = {},pageSize = {} , orderId = {},beginTiem= {} , endTime = {}",page,pageSize,number,beginTime,endTime);
        //1、构造分页构造器
        Page<Orders> pageInfo = new Page(page,pageSize);
        Page<OrderDetail> orderDetailPage = new Page<>();
        SimpleDateFormat  simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //2、构造条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(number!=null,Orders::getNumber,number);
        if (beginTime!=null && endTime!=null){
            Date begin = simpleDateFormat.parse(beginTime);
            Date end = simpleDateFormat.parse(endTime);
            queryWrapper.between(Orders::getOrderTime,begin,end);
        }

        //3、添加排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime);

        orderService.page(pageInfo,queryWrapper);
        
        return R.success(pageInfo);
    }

    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize){
        log.info("page = {},pageSize = {}",page,pageSize);
        //1、构造分页构造器
        Page<Orders> pageInfo = new Page(page,pageSize);
        Page<OrdersDto> orderDetailPage = new Page<>();

        //2、构造条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper();

        //3、添加排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime);

        orderService.page(pageInfo,queryWrapper);

        BeanUtils.copyProperties(pageInfo,orderDetailPage,"records");
        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> list = records.stream().map((item)->{
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item,ordersDto);
            Long ordersId = item.getId();
            LambdaQueryWrapper<OrderDetail> detailQueryWrapper = new LambdaQueryWrapper();
            detailQueryWrapper.eq(OrderDetail::getOrderId,ordersId);
            List<OrderDetail> orderDetailList = orderDetailService.list(detailQueryWrapper);
            if (orderDetailList!=null){
                ordersDto.setOrderDetails(orderDetailList);
            }
            return ordersDto;
        }).collect(Collectors.toList());
        orderDetailPage.setRecords(list);
        return R.success(orderDetailPage);
    }
}
