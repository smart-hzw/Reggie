package com.hzw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hzw.common.R;
import com.hzw.common.ValidateCodeUtils;
import com.hzw.pojo.User;
import com.hzw.service.SendEmailToUserService;
import com.hzw.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;
    /**
     * 发送手机短信验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpServletRequest request){
        log.info("当前邮箱为：{}",user.getPhone());
        //获取手机号
        String userEmail = user.getPhone();

        //判断当前获取的手机号是否不为空
        if (!StringUtils.isEmpty(userEmail)){
            //生成随机6位验证码
            String code = ValidateCodeUtils.generateValidateCode(6).toString();
            String message = "验证码:"+code+",有效期为5分钟。您的Reggie外卖帐号正在登录应用，如非本人操作，请立即更改密码。";
            //调用邮件发送工具类
            sendEmailToUserService.sendEmail(userEmail,message);

            //需要将生成的验证码保存Session
            request.getSession().setAttribute(userEmail,code);

            //将生成的验证码缓存到redis中，并且设置有效期为5分钟
            redisTemplate.opsForValue().set(userEmail,code, 5,TimeUnit.MINUTES);
            return R.success("邮件发送成功");
        }
        return R.error("邮箱为空");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info("用户登录信息为,{}",map);
        String userEmail = (String) map.get("phone");
        String code = (String) map.get("code");
        //从session中获取验证码
        //String emailCode = (String) session.getAttribute(userEmail);
        //从redis中获取验证码
        Object emailCode = redisTemplate.opsForValue().get(userEmail);
        if (code.equals(emailCode) && emailCode!=null){
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
            //如果用户登录成功，就可以删除redis中缓存的验证码
            redisTemplate.delete(userEmail);
            return R.success(user);
        }
        return R.error("登录失败");
    }
}
