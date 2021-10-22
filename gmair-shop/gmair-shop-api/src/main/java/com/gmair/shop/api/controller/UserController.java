

package com.gmair.shop.api.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.hutool.core.util.StrUtil;
import com.gmair.shop.bean.app.param.UserPhoneParam;
import com.gmair.shop.common.exception.GmairShopGlobalException;
import com.gmair.shop.common.util.CacheManagerUtil;
import com.gmair.shop.security.enums.App;
import com.gmair.shop.security.model.AppConnect;
import com.gmair.shop.security.service.AppConnectService;
import com.gmair.shop.security.service.GmairUserDetailsService;
import com.gmair.shop.security.util.SecurityUtils;
import com.gmair.shop.service.feign.MembershipFeignService;
import com.gmair.shop.service.feign.UserFeignService;
import finley.gmair.model.consumer.Consumer;
import finley.gmair.form.consumer.ConsumerForm;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResponseData;
import finley.gmair.util.ResultData;
import finley.gmair.vo.consumer.ConsumerPartInfoVo;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

import com.gmair.shop.bean.app.dto.UserDto;
import com.gmair.shop.bean.app.param.UserInfoParam;
import com.gmair.shop.bean.model.User;
import com.gmair.shop.service.UserService;

import cn.hutool.extra.emoji.EmojiUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/shop/consumer/user")
@Api(tags="用户接口")
@AllArgsConstructor
public class UserController {

	private final UserService userService;

	private final MapperFacade mapperFacade;

	private final CacheManagerUtil cacheManagerUtil;

	private final ConsumerTokenServices consumerTokenServices;

	private final AppConnectService appConnectService;

	private final GmairUserDetailsService gmairUserDetailsService;

	private final WxMaService wxMaService;

	private final UserFeignService userFeignService;

	private final MembershipFeignService membershipFeignService;
	/**
	 * 查看用户接口
	 */
	@GetMapping("/userInfo")
	@ApiOperation(value="查看用户信息", notes="根据用户ID（userId）获取用户信息")
	public ResponseEntity<UserDto> userInfo() {
		String userId = SecurityUtils.getUser().getUserId();
		User user = userService.getById(userId);
		UserDto userDto = mapperFacade.map(user, UserDto.class);
		return ResponseEntity.ok(userDto);
	}

	@PutMapping("/setUserInfo")
	@ApiOperation(value="设置用户信息", notes="设置用户信息")
	public ResponseEntity<Void> setUserInfo(@RequestBody UserInfoParam userInfoParam) {
		String userId = SecurityUtils.getUser().getUserId();
		User user = new User();
		user.setUserId(userId);
		user.setPic(userInfoParam.getAvatarUrl());
		user.setNickName(EmojiUtil.toAlias(userInfoParam.getNickName()));
		userService.updateById(user);
		String cacheKey = App.MINI.value() + StrUtil.COLON + SecurityUtils.getUser().getBizUserId();
		cacheManagerUtil.evictCache("gmair_user", cacheKey);
		return ResponseEntity.ok(null);
	}

	/**
	 * 退出登录,并清除redis中的token
	 **/
	@GetMapping("/logout")
	public Boolean removeToken(HttpServletRequest httpRequest){
		String authorization = httpRequest.getHeader("authorization");
		String token = authorization.replace("bearer", "");
		String userId = SecurityUtils.getUser().getUserId();
		AppConnect appConnect =appConnectService.getByUserId(userId,App.MINI);
		gmairUserDetailsService.deleteSessionkey(appConnect.getBizUserId());
		return consumerTokenServices.revokeToken(token);
	}



	/**
	 * set user's phone number
	 **/
	@PutMapping("/setUserPhone")
	public ResponseEntity<String> setUserPhone(@RequestBody UserPhoneParam userPhoneParam){
		String userId = SecurityUtils.getUser().getUserId();
		AppConnect appConnect =appConnectService.getByUserId(userId,App.MINI);
		String sessionKey = gmairUserDetailsService.setOrGetSessionkey(appConnect.getBizUserId(),"");
		WxMaPhoneNumberInfo phoneNoInfo;
		try{
			phoneNoInfo = wxMaService.getUserService().getPhoneNoInfo(sessionKey, userPhoneParam.getEncryptedData(), userPhoneParam.getIv());
		}catch (Exception e){
			throw new GmairShopGlobalException("手机号获取失败");
			// if there throw a Exception, the below code can't be run
		}

		if(phoneNoInfo==null||StrUtil.isBlank(phoneNoInfo.getPhoneNumber())||StrUtil.isBlank(phoneNoInfo.getCountryCode())){
			throw new GmairShopGlobalException("绑定手机号失败");
		}

		User user = new User();
		user.setUserId(userId);
		user.setUserMobile(phoneNoInfo.getPhoneNumber());
		user.setCountryCode(phoneNoInfo.getCountryCode());
		userService.updateById(user);
		return ResponseEntity.ok(user.getUserMobile());

	}
	/**
	 * @Description get consumer_id from module auth-consumer
	 * @Date  2021/10/14 16:34
	 * @return org.springframework.http.ResponseEntity<java.lang.String>
	 */
	@PutMapping("/setConsumerId")
	public ResponseEntity<Void> setConsumerId(){
		String userId = SecurityUtils.getUser().getUserId();
		User user = userService.getUserByUserId(userId);
		// get consumer_id from module auth-consumer
		ResponseData<ConsumerPartInfoVo> resultData = userFeignService.getConsumerInfoByPhone(user.getUserMobile());
		AppConnect appConnect =appConnectService.getByUserId(userId,App.MINI);
		if(resultData.getResponseCode()== ResponseCode.RESPONSE_NULL){// no consumer in auth-consumer
		    String realName =user.getRealName();
		    if(StringUtils.isBlank(realName)){
		    	realName = user.getNickName();
			}
			ResultData result = userFeignService.consumerRegister(realName,user.getNickName(),user.getUserMobile(),appConnect.getBizUserId());// create new consumer
			if(result.getResponseCode()!=ResponseCode.RESPONSE_OK){
				throw new GmairShopGlobalException(result.getDescription());
			}
			Consumer consumer = (Consumer) result.getData();
			user.setConsumerId(consumer.getConsumerId());
		}else if(resultData.getResponseCode()== ResponseCode.RESPONSE_ERROR){
			throw new GmairShopGlobalException("用户绑定失败");
		}else{ // success to get ConsumerId and other info
			ConsumerPartInfoVo consumerPartInfoVo =resultData.getData();
			user.setConsumerId(consumerPartInfoVo.getConsumerId());
			user.setRealName(consumerPartInfoVo.getName());
		}
		userService.updateById(user);
		return ResponseEntity.ok().build();
	}
}
