package finley.gmair.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;

/**
 * 用户权限配置相关
 * 参照finley.gmair.config.ReceptionSecurityConfig[reception]
 */
@Configuration
@EnableResourceServer
public class TmallGenieSecurityConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .authorizeRequests()
                .antMatchers("/tmallgenie/voice/control").permitAll()
                .antMatchers("/tmallgenie/list/update").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }
}
