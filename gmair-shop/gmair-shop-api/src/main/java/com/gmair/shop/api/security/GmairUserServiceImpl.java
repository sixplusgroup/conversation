

package com.gmair.shop.api.security;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.emoji.EmojiUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gmair.shop.bean.model.MembershipUser;
import com.gmair.shop.bean.model.User;
import com.gmair.shop.common.annotation.RedisLock;
import com.gmair.shop.common.exception.GmairShopGlobalException;
import com.gmair.shop.common.util.PrincipalUtil;
import com.gmair.shop.dao.UserMapper;
import com.gmair.shop.security.dao.AppConnectMapper;
import com.gmair.shop.security.enums.App;
import com.gmair.shop.security.exception.UsernameNotFoundException;
import com.gmair.shop.security.exception.UsernameNotFoundExceptionBase;
import com.gmair.shop.security.model.AppConnect;
import com.gmair.shop.security.entity.GmairUser;
import com.gmair.shop.security.service.GmairUserDetailsService;
import com.gmair.shop.service.MembershipService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 用户详细信息
 *
 *
 */
@Slf4j
@Service
@AllArgsConstructor
public class GmairUserServiceImpl implements GmairUserDetailsService {

	private final UserMapper userMapper;

	private final AppConnectMapper appConnectMapper;

	private final PasswordEncoder passwordEncoder;

	private final MembershipService membershipService;
	@Override
	@SneakyThrows
	public GmairUser loadUserByUsername(String username) {
		if (StrUtil.isBlank(username) || !username.contains(StrUtil.COLON) ) {
			throw new UsernameNotFoundExceptionBase("无法获取用户信息");
		}
		String[] splitInfo = username.split(StrUtil.COLON);
		App app = App.instance(Integer.valueOf(splitInfo[0]));
		String bizUserId = splitInfo[1];
		return loadUserByAppIdAndBizUserId(app,bizUserId);
	}

	/**
	 * 获取前端登陆的用户信息
	 *
	 * @param app
	 * @param bizUserId openId
	 * @return UserDetails
	 * @throws UsernameNotFoundExceptionBase
	 */
	@Override
	public GmairUser loadUserByAppIdAndBizUserId(App app, String bizUserId) {

		String cacheKey = app.value() + StrUtil.COLON + bizUserId;

		User user = userMapper.getUserByBizUserId(app.value(), bizUserId);
		if (user == null) {
			throw new UsernameNotFoundExceptionBase("无法获取用户信息");
		}
		String name = StrUtil.isBlank(user.getRealName()) ? user.getNickName() : user.getRealName();
		GmairUser gmairUser = new GmairUser(user.getUserId(), bizUserId, app.value(), user.getStatus() == 1);
		gmairUser.setName(name);
		gmairUser.setPic(user.getPic());

		return gmairUser;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@RedisLock(lockName = "insertUser", key = "#appConnect.appId + ':' + #appConnect.bizUserId")
	@Caching(evict = {
			@CacheEvict(cacheNames = "gmair_user", key = "#appConnect.appId + ':' + #appConnect.bizUserId"),
			@CacheEvict(cacheNames = "AppConnect", key = "#appConnect.appId + ':' + #appConnect.bizUserId")
	})
	public void insertUserIfNecessary(AppConnect appConnect) {
		// 进入锁后再重新判断一遍用户是否创建
		AppConnect dbAppConnect = appConnectMapper.getByBizUserId(appConnect.getBizUserId(), appConnect.getAppId());
		if(dbAppConnect != null) {
			return;
		}

		String bizUnionId = appConnect.getBizUnionid();
		String userId = null;
		User user;

		if (StrUtil.isNotBlank(bizUnionId)) {
			userId = appConnectMapper.getUserIdByUnionId(bizUnionId);
		}
		if (StrUtil.isBlank(userId)) {
			userId = IdUtil.simpleUUID();
			Date now = new Date();
			user = new User();
			user.setUserId(userId);
			user.setModifyTime(now);
			user.setUserRegtime(now);
			user.setStatus(1);
			user.setNickName(EmojiUtil.toAlias(StrUtil.isBlank(appConnect.getNickName()) ? "" : appConnect.getNickName()));
			user.setPic(appConnect.getImageUrl());
			userMapper.insert(user);
		} else {
			user = userMapper.selectById(userId);
		}

		appConnect.setUserId(user.getUserId());
		// 微信小程序登录成功, 记录登录信息 openid等等
		appConnectMapper.insert(appConnect);
		// 让用户成为普通会员
		if(StrUtil.isBlank(user.getUserId())){
			throw new GmairShopGlobalException("用户ID不可为空");
		}
		if(membershipService.count(new LambdaQueryWrapper<MembershipUser>().eq(MembershipUser::getUserId, userId))!=0){
			throw new GmairShopGlobalException("该用户已是会员, 请联系管理员处理!");
		}
		membershipService.createMembership(user.getUserId());

	}

	@Override
	public GmairUser loadUserByUserMail(String userMail, String loginPassword) {
		User user = userMapper.getUserByUserMail(userMail);
		if (user == null) {
			throw new UsernameNotFoundException("用户不存在");
		}

		if (!passwordEncoder.matches(loginPassword, user.getLoginPassword())) {
			// 原密码不正确
			throw new UsernameNotFoundException("密码不正确");
		}
		String name = StrUtil.isBlank(user.getRealName()) ? user.getNickName() : user.getRealName();
		GmairUser gmairUser = new GmairUser(user.getUserId(), loginPassword, user.getStatus() == 1);
		gmairUser.setName(name);
		gmairUser.setPic(user.getPic());
		return gmairUser;
	}

	@Override
	public User loadUserByMobileOrUserName(String mobileOrUserName, Integer loginType) {
		User user = null;
		// 手机验证码登陆，或传过来的账号很像手机号
		if (Objects.equals(loginType, 1) || PrincipalUtil.isMobile(mobileOrUserName)) {
			user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserMobile, mobileOrUserName));
		}
		return user;
	}

	@Override
	public GmairUser getGmairUser(Integer appId, User user, String bizUserId) {
		String name = StrUtil.isBlank(user.getRealName()) ? user.getNickName() : user.getRealName();
		GmairUser gmairUser = new GmairUser();
		gmairUser.setEnabled(user.getStatus() == 1);
		gmairUser.setUserId(user.getUserId());
		gmairUser.setBizUserId(bizUserId);
		gmairUser.setAppType(appId);
		gmairUser.setName(name);
		gmairUser.setPic(user.getPic());
		gmairUser.setPassword(user.getLoginPassword());
		return gmairUser;
	}
}
