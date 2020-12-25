package finley.gmair.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
public class OAuth2ServerConfig {

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                    // Since we want the protected resources to be accessible in the UI as well we need
                    // session creation to be allowed (it's disabled by default in 2.0.6)
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .and()
                    .requestMatchers().anyRequest()
                    .and()
                    .anonymous()
                    .and()
                    .authorizeRequests()
                    .antMatchers("/auth/consumer/request").permitAll()
                    .antMatchers("/auth/consumer/register").permitAll()
                    .antMatchers("/auth/consumer/profile").permitAll()
                    .antMatchers("/auth/probe/consumerid/by/openid").permitAll()
                    .antMatchers("/auth/user/**").authenticated();
            // @formatter:on
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
        @Autowired
        AuthenticationManager authenticationManager;


        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            //配置两个客户端,一个用于password认证一个用于client认证
            clients.inMemory().withClient("client_1")
                    .resourceIds()
                    .authorizedGrantTypes("client_credentials", "refresh_token")
                    .scopes("select")
                    .authorities("client")
                    .secret("123456")
                    .and()
                    .withClient("client_2")
                    .resourceIds()
                    .authorizedGrantTypes("password", "refresh_token")
                    .scopes("select")
                    .authorities("client")
                    .secret("123456")
                    .and()
                    .withClient("client_3")
                    .resourceIds()
                    .authorizedGrantTypes("authorization_code", "refresh_token")
                    .autoApprove(true)//用户自动同意授权
                    .redirectUris("https://oauth-redirect.api.home.mi.com/r/4453")
                    .scopes("select")
                    .authorities("client")
                    .secret("123456")
                    .and()
                    .withClient("client_4")
                    .resourceIds()
                    .authorizedGrantTypes("authorization_code", "refresh_token")
                    .autoApprove(true)//用户自动同意授权
                    .redirectUris("https://open.bot.tmall.com/oauth/callback")
                    .scopes("select")
                    .authorities("client")
                    .secret("123456");
        }


        @Bean
        @Primary
        public DefaultTokenServices defaultTokenServices() {
            DefaultTokenServices services = new DefaultTokenServices();
            services.setSupportRefreshToken(true);
            services.setAccessTokenValiditySeconds(60 * 60 * 48);//设置token的过期时间
            services.setRefreshTokenValiditySeconds(60 * 60 * 48);
            services.setTokenStore(tokenStore());
            return services;
        }

        @Bean
        public TokenStore tokenStore() {
            return new InMemoryTokenStore();
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .tokenStore(tokenStore())
                    .authenticationManager(authenticationManager)
                    .tokenServices(defaultTokenServices());
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            //允许表单认证
            oauthServer.allowFormAuthenticationForClients();
        }
    }
}
