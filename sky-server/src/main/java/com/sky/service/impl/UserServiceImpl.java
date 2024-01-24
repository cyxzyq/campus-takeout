package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.entity.User;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//用户端
@Service
public class UserServiceImpl implements UserService {

    private static final String URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    WeChatProperties weChatProperties;
    @Autowired
    UserMapper userMapper;

    //用户登录
    @Override
    public User userLogin(String code) {
        //调用微信接口服务，获取openid
        String openid = getOpenid(code);
        //判断openid是否为null，为null抛登录异常
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //判断当前用户是否为新用户
        User user = userMapper.findUserByOpenid(openid);

        //若为新用户向数据库user表插入用户数据
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setCreateTime(LocalDateTime.now());
            userMapper.addUser(user);
        }

        return user;
    }

    //调用微信接口服务获取openid
    private String getOpenid(String code) {
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        //发送异步请求调用微信小程序提供的API获取用户的唯一标识openid;
        String msg = HttpClientUtil.doGet(URL, map);
        JSONObject jsonObject = JSON.parseObject(msg);
        //获取json字符串指定的openid值
        String openid = jsonObject.getString("openid");
        return openid;
    }
}
