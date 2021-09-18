

package com.gmair.shop.admin.security;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gmair.shop.bean.model.User;
import com.gmair.shop.common.util.CacheManagerUtil;
import com.gmair.shop.sys.constant.Constant;
import com.gmair.shop.security.enums.App;
import com.gmair.shop.security.exception.UsernameNotFoundExceptionBase;
import com.gmair.shop.security.model.AppConnect;
import com.gmair.shop.security.entity.GmairSysUser;
import com.gmair.shop.security.entity.GmairUser;
import com.gmair.shop.security.service.GmairUserDetailsService;
import com.gmair.shop.sys.dao.SysMenuMapper;
import com.gmair.shop.sys.dao.SysUserMapper;
import com.gmair.shop.sys.model.SysMenu;
import com.gmair.shop.sys.model.SysUser;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户详细信息
 *
 *
 */
@Slf4j
@Service
@AllArgsConstructor
public class GmairSysUserDetailsServiceImpl implements GmairUserDetailsService {
	final String me = "GmairSysUserDetailsServiceImpl";
	private final SysMenuMapper sysMenuMapper;
	private final SysUserMapper sysUserMapper;
	private final CacheManagerUtil cacheManagerUtil;

	/**
	 * 用户密码登录
	 *
	 * @param username 用户名
	 * @return
	 * @throws UsernameNotFoundExceptionBase
	 */
	@Override
	@SneakyThrows
	public GmairSysUser loadUserByUsername(String username) {
		return getUserDetails(username);
	}


	/**
	 * 构建userdetails
	 *
	 * @param username 用户名称
	 * @return
	 */
	private GmairSysUser getUserDetails(String username) {
		SysUser sysUser = sysUserMapper.selectByUsername(username);

		if (sysUser == null) {
			throw new UsernameNotFoundExceptionBase("用户不存在");
		}

		Collection<? extends GrantedAuthority> authorities
				= AuthorityUtils.createAuthorityList(getUserPermissions(sysUser.getUserId()).toArray(new String[0]));
		// 构造security用户
		return new GmairSysUser(sysUser.getUserId(), sysUser.getShopId(), sysUser.getUsername(), sysUser.getPassword(), sysUser.getStatus() == 1,
				true, true, true , authorities);
	}

	private Set<String> getUserPermissions(Long userId) {
		List<String> permsList;

		//系统管理员，拥有最高权限
		if(userId == Constant.SUPER_ADMIN_ID){
			List<SysMenu> menuList = sysMenuMapper.selectList(Wrappers.emptyWrapper());


			permsList = menuList.stream().map(SysMenu::getPerms).collect(Collectors.toList());
		}else{
			permsList = sysUserMapper.queryAllPerms(userId);
		}


		Set<String> permsSet = permsList.stream().flatMap((perms)->{
					if (StrUtil.isBlank(perms)) {
						return null;
					}
					return Arrays.stream(perms.trim().split(","));
				}
		).collect(Collectors.toSet());
		return permsSet;
	}

	@Override
	public GmairUser loadUserByAppIdAndBizUserId(App app, String bizUserId) {
		return null;
	}

	@Override
	public void insertUserIfNecessary(AppConnect appConnect) {

	}

	@Override
	public GmairUser loadUserByUserMail(String userMail, String loginPassword) {
		return null;
	}

	@Override
	public User loadUserByMobileOrUserName(String mobileOrUserName, Integer loginType) {
		return null;
	}

    @Override
    public GmairUser getGmairUser(Integer appId, User user, String bizUserId) {
        return null;
    }
}
