

package com.gmair.shop.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gmair.shop.bean.model.User;
import com.gmair.shop.bean.param.UserRegisterParam;
import com.gmair.shop.common.exception.GmairShopGlobalException;
import com.gmair.shop.common.util.RedisUtil;
import com.gmair.shop.dao.UserMapper;
import com.gmair.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    @Cacheable(cacheNames = "user", key = "#userId")
    public User getUserByUserId(String userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public Boolean insertUser(UserRegisterParam uParam) {
        User mail = userMapper.getUserByUserMail(uParam.getUserMail());
        if (mail != null) {
            throw new GmairShopGlobalException("账号已存在");
        }
        Date now = new Date();
        User user = new User();
        user.setUserId(IdUtil.simpleUUID());
        user.setModifyTime(now);
        user.setUserRegtime(now);
        user.setStatus(1);
        user.setNickName(uParam.getUserMail());
        user.setUserMail(uParam.getUserMail());
        user.setLoginPassword(uParam.getPassword());
        return userMapper.insert(user) == 1;
    }
    /**
     * 看看有没有校验验证码成功的标识
     * @param userRegisterParam
     * @param checkRegisterSmsFlag
     */
    @Override
    public void validate(UserRegisterParam userRegisterParam, String checkRegisterSmsFlag) {
        if (StrUtil.isBlank(userRegisterParam.getCheckRegisterSmsFlag())) {
            // 验证码已过期，请重新发送验证码校验
            throw new GmairShopGlobalException("验证码已过期，请重新发送验证码校验");
        } else {
            String checkRegisterSmsFlagMobile = RedisUtil.get(checkRegisterSmsFlag);
            if (!Objects.equals(checkRegisterSmsFlagMobile, userRegisterParam.getMobile())) {
                // 验证码已过期，请重新发送验证码校验
                throw new GmairShopGlobalException("验证码已过期，请重新发送验证码校验");
            }
        }
    }
}
