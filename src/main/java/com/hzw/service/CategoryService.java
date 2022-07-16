package com.hzw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzw.pojo.Category;
import com.hzw.pojo.Employee;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
