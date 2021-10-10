

package com.gmair.shop.security.controller;


import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.gmair.shop.security.constants.SecurityConstants;

import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gmair.shop.common.util.RedisUtil;
import com.gmair.shop.common.util.SimpleCaptcha;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 登录相关
 *
 */
@Controller
@AllArgsConstructor
@ApiIgnore
public class SysLoginController {

	private final CacheManager cacheManager;


	@GetMapping("/captcha.jpg")
	public void login(HttpServletResponse response,String uuid) {
		//定义图形验证码的长、宽、验证码字符数、干扰元素个数
		SimpleCaptcha simpleCaptcha = new SimpleCaptcha(200, 50, 4, 20);
		try {
			simpleCaptcha.write(response.getOutputStream());
			RedisUtil.set(SecurityConstants.SPRING_SECURITY_RESTFUL_IMAGE_CODE+uuid, simpleCaptcha.getCode(), 300);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 退出
	 */
	@PostMapping(value = "/sys/logout")
	public ResponseEntity<String> logout() {
		SecurityContextHolder.clearContext();
		return ResponseEntity.ok().build();
	}

}
