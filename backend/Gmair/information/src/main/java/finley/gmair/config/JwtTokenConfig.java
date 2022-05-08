package finley.gmair.config;

import finley.gmair.oauth2.CustomUser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class JwtTokenConfig {
    @Bean
    JwtTokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean("myJwtTokenConverter")
    JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                Map<String, Object> additionalInformation = new LinkedHashMap<>();
                Map<String, Object> info = new LinkedHashMap<>();
                //这里添加你的参数
                info.put("id", ((CustomUser) authentication.getPrincipal()).getId());
                info.put("username", ((CustomUser) authentication.getPrincipal()).getUsername());
                info.put("type", ((CustomUser) authentication.getPrincipal()).getType());

                additionalInformation.put("info", info);
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
                return super.enhance(accessToken, authentication);
            }
        };
        converter.setSigningKey("www.javaboy.org");
        return converter;
    }

    @Bean("myJwtTokenService")
    AuthorizationServerTokenServices tokenServices(@Qualifier("myJwtTokenConverter") JwtAccessTokenConverter converter,
                                                   JwtTokenStore tokenStore) {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setSupportRefreshToken(true);
        services.setTokenStore(tokenStore);
        TokenEnhancerChain chain = new TokenEnhancerChain();
        chain.setTokenEnhancers(Arrays.asList(converter));
        services.setTokenEnhancer(chain);
        return services;
    }

}
