

package com.gmair.shop.security.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gmair.shop.bean.model.User;
import com.gmair.shop.common.exception.GmairShopGlobalException;
import com.gmair.shop.dao.UserMapper;
import com.gmair.shop.security.dao.AppConnectMapper;
import com.gmair.shop.security.enums.App;
import com.gmair.shop.security.model.AppConnect;
import com.gmair.shop.security.service.AppConnectService;
import com.gmair.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;


/**
 *
 *
 */
@Service
public class AppConnectServiceImpl extends ServiceImpl<AppConnectMapper, AppConnect> implements AppConnectService {

    @Autowired
    private AppConnectMapper appConnectMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserService userService;

	/**
	 * GmairUserServiceImpl#insertUserIfNecessary 将会清楚该缓存信息
	 * @param bizUserId
	 * @param app
	 * @return
	 */
	@Override
	@Cacheable(cacheNames = "AppConnect", key = "#app.value() + ':' + #bizUserId")
	public AppConnect getByBizUserId(String bizUserId, App app) {
		return appConnectMapper.getByBizUserId(bizUserId, app.value());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public User registerOrBindUser(User user, AppConnect appConnect, Integer appId) {
		if (StrUtil.isBlank(user.getUserId())) {
			if (userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUserMobile, user.getUserMobile())) > 0) {
				// 该电话号码已存在
				throw new GmairShopGlobalException("该电话号码已存在");
			}
			String userId = IdUtil.simpleUUID();
			user.setUserId(userId);
			userMapper.insert(user);
		} else {
			if (appConnect != null&& StrUtil.isBlank(user.getPic())) {
				User userParam = new User();
				userParam.setUserId(user.getUserId());
				userParam.setModifyTime(new Date());
				userParam.setPic(appConnect.getImageUrl());
				userService.updateById(userParam);
			}
		}
		if (appConnect == null) {
			// 避免重复插入数据
			if (appConnectMapper.getByBizUserId(user.getUserId(), appId) != null) {
				return user;
			}
			appConnect = new AppConnect();
			appConnect.setUserId(user.getUserId());
			appConnect.setNickName(user.getNickName());
			appConnect.setImageUrl(user.getPic());

			// 0表示是系统的用户，不是第三方的
			appConnect.setAppId(appId);
			appConnectMapper.insert(appConnect);
		} else if (StrUtil.isBlank(appConnect.getUserId()) || Objects.isNull(appId)) {
			appConnect.setAppId(appId);
			appConnect.setUserId(user.getUserId());
			appConnect.setUserId(user.getUserId());
			appConnectMapper.updateById(appConnect);
		}

		return user;
	}
}
