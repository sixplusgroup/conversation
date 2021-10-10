

package com.gmair.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gmair.shop.bean.model.User;
import com.gmair.shop.bean.param.UserRegisterParam;
import com.gmair.shop.bean.vo.UserVO;

/**
 *
 *
 */
public interface UserService extends IService<User> {

    User getUserByUserId(String userId);

    Boolean insertUser(UserRegisterParam userRegisterParam);

    void validate(UserRegisterParam userRegisterParam, String checkRegisterSmsFlag);
}
