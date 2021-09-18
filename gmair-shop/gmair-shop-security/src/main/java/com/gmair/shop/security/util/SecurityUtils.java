

package com.gmair.shop.security.util;


import com.gmair.shop.security.exception.UnauthorizedExceptionBase;
import com.gmair.shop.security.entity.GmairSysUser;
import com.gmair.shop.security.entity.GmairUser;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全工具类
 *
 *
 */
@UtilityClass
public class SecurityUtils {
	/**
	 * 获取Authentication
	 */
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取用户
	 */
	public GmairUser getUser() {
		Authentication authentication = getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof GmairUser) {
			return (GmairUser) principal;
		}
		throw new UnauthorizedExceptionBase("无法获取普通用户信息");
	}

	/**
	 * 获取系统用户
	 */
	public GmairSysUser getSysUser() {
		Authentication authentication = getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof GmairSysUser) {
			return (GmairSysUser) principal;
		}
		throw new UnauthorizedExceptionBase("无法获取系统用户信息");
	}
}
