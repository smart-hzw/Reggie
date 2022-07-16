package com.hzw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzw.common.CustomException;
import com.hzw.mapper.AddressBookMapper;
import com.hzw.mapper.DishMapper;
import com.hzw.pojo.AddressBook;
import com.hzw.pojo.Dish;
import com.hzw.pojo.DishDto;
import com.hzw.pojo.DishFlavor;
import com.hzw.service.AddressBookService;
import com.hzw.service.DishFlavorService;
import com.hzw.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
