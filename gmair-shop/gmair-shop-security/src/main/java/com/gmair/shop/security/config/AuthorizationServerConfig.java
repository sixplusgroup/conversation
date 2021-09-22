

package com.gmair.shop.security.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 *  Authorization授权配置
 */
@Configuration
//@Order(2)
@EnableAuthorizationServer // 设置为授权服务器
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthorizationServerTokenServices gmairTokenServices;

    @Autowired
    private TokenEnhancer tokenEnhancer;

    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)  // 配置认证 manager
                .tokenStore(tokenStore)// 从 token 中读取特定字段构成 Authentication
                .tokenEnhancer(tokenEnhancer)// token增强, 加入额外信息
                // refresh_token需要userDetailsService
                .reuseRefreshTokens(false)
                .userDetailsService(userDetailsService);
                // UserDetailsService两个实现类不会冲突?
                // 两个实现类属于不同的application, 两个@Configuration AuthorizationServerConfig 变成配置Bean也是在不同的容器
        endpoints.tokenServices(gmairTokenServices);
    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                // 开启/oauth/token_key验证端口无权限访问
                .tokenKeyAccess("permitAll()") // 请求token的url 允许
                // 开启/oauth/check_token验证端口认证权限访问
                .checkTokenAccess("isAuthenticated()"); // 验证token的都是应该认证过的
    }






}
