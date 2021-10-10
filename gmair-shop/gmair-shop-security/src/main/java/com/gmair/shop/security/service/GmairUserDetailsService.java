

package com.gmair.shop.security.service;

import com.gmair.shop.bean.model.User;
import com.gmair.shop.security.entity.GmairUser;
import com.gmair.shop.security.enums.App;
import com.gmair.shop.security.exception.UsernameNotFoundExceptionBase;
import com.gmair.shop.security.model.AppConnect;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 用户详细信息
 *
 *
 */
public interface GmairUserDetailsService extends UserDetailsService {// 两个实现类

	/**
	 * 获取前端登陆的用户信息
	 *
	 * @param app 所登陆的应用
	 * @param bizUserId openId
	 * @return UserDetails
	 * @throws UsernameNotFoundExceptionBase
	 */
	GmairUser loadUserByAppIdAndBizUserId(App app, String bizUserId);

	/**
	 * 如果必要的话，插入新增用户
	 * @param appConnect
	 */
	void insertUserIfNecessary(AppConnect appConnect);

	/**
	 * 账号、密码登录
	 * @param userMail
	 * @param loginPassword
	 * @return
	 */
	GmairUser loadUserByUserMail(String userMail, String loginPassword);

	User loadUserByMobileOrUserName(String mobileOrUserName, Integer loginType);

	GmairUser getGmairUser(Integer appId, User user, String bizUserId);

	void deleteSessionkey(String bizUserId);

	String setOrGetSessionkey(String bizUserId,String sessionKey);



}
