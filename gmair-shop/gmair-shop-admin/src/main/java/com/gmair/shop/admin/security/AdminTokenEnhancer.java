package com.gmair.shop.admin.security;

import com.gmair.shop.security.entity.GmairSysUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * token增强
 *
 */
@Component
public class AdminTokenEnhancer implements TokenEnhancer {


    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>(8);
        GmairSysUser gmairSysUser = (GmairSysUser) authentication.getUserAuthentication().getPrincipal();
        additionalInfo.put("shopId", gmairSysUser.getShopId());
        additionalInfo.put("userId", gmairSysUser.getUserId());
        additionalInfo.put("authorities", gmairSysUser.getAuthorities());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
