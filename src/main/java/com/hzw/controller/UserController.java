package com.hzw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hzw.common.R;
import com.hzw.common.ValidateCodeUtils;
import com.hzw.pojo.User;
import com.hzw.service.SendEmailToUserService;
import com.hzw.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private SendEmailToUserService sendEmailToUserService;

    @Resource
    private RedisCacheManager RedisCacheManager;

    //@Autowired
    //private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    /**
     * 发送手机短信验证码
     * @param user
     * @return
     */
    @CachePut(value = "UserLogin",key = "#user.phone")
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user){
        log.info("当前邮箱为：{}",user.getPhone());
        //获取邮箱地址
        String userEmail = user.getPhone();

        //判断当前获取的邮箱地址是否不为空
        if (!StringUtils.isEmpty(userEmail)){
            //生成随机6位验证码
            String code = ValidateCodeUtils.generateValidateCode(6).toString();
            String message = "验证码:"+code+",有效期为5分钟。您的Reggie外卖帐号正在登录应用，如非本人操作，请立即更改密码。";
            //调用邮件发送工具类
            sendEmailToUserService.sendEmail(userEmail,message);

            return R.success(code);
        }
        return R.error("邮箱为空");
    }

    @CacheEvict(value = "UserLogin",key = "#map.get('phone')")
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info("用户登录信息为,{}",map);
        String userEmail = (String) map.get("phone");
        String code = (String) map.get("code");
        //从redis中获取验证码
        Cache cache = RedisCacheManager.getCache("UserLogin");
        Cache.ValueWrapper valueWrapper = cache.get(userEmail);
        R r = (R) valueWrapper.get();
        Object cacheCode = r.getData();
        log.info("获取到的缓存为：{},..........,{}",cache.toString(),cacheCode);

        if (code.equals(cacheCode) && cacheCode!=null){
            //判断当前用户对应的用户是否为新用户，如果是新用户自动完成注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,userEmail);
            User user = userService.getOne(queryWrapper);
            if (user == null){
                user = new User();
                user.setPhone(userEmail);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }
}
