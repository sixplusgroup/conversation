

package com.gmair.shop.security.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.gmair.shop.bean.model.User;
import com.gmair.shop.security.enums.App;
import com.gmair.shop.security.model.AppConnect;

/**
 *
 *
 */
public interface AppConnectService extends IService<AppConnect> {

	AppConnect getByBizUserId(String bizUserId, App app);

	User registerOrBindUser(User user, AppConnect appConnect, Integer appId);

	AppConnect getByUserId(String userId, App app);

}
