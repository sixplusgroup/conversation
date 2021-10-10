package com.gmair.shop.api.security;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.emoji.EmojiUtil;
import com.gmair.shop.security.entity.GmairUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * token增强,加入一些額外的信息,注意key不要重复
 *
 */
@Component
public class ApiTokenEnhancer implements TokenEnhancer {


    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>(8);
        GmairUser gmairUser = (GmairUser) authentication.getUserAuthentication().getPrincipal();
        additionalInfo.put("userId", gmairUser.getUserId());
        additionalInfo.put("nickName", EmojiUtil.toUnicode(StrUtil.isBlank(gmairUser.getName())? "" : gmairUser.getName()));
        additionalInfo.put("pic",gmairUser.getPic());
        additionalInfo.put("enabled",gmairUser.isEnabled());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
