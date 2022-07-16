package com.hzw.service.impl;

import com.hzw.service.SendEmailToUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendEmailToUserServiceImpl implements SendEmailToUserService {
    @Autowired
    private JavaMailSender javaMailSender;
    //邮件几要素
    //收件人
    private String from = "hzw2510@163.com";
    //发件人
    //标题
    private String title = "reggie外卖验证码";
    //正文


    @Override
    public void sendEmail(String userEmail,String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from+"(Reggie外卖)");
        message.setTo(userEmail);
        message.setSubject(title);
        message.setText(code);
        javaMailSender.send(message);
    }
}
