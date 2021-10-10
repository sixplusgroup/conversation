

package com.gmair.shop.mp.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import com.gmair.shop.mp.component.WxMaInRedisConfig;
import com.gmair.shop.mp.component.WxMaServiceClusterImpl;
import lombok.AllArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信小程序配置文件
 *
 */
@Configuration
@AllArgsConstructor
@ConditionalOnClass(WxMaService.class)
public class WxMaConfiguration {


    private final WxMaInRedisConfig wxMaInRedisConfig;

    private final RedissonClient redissonClient;

    @Bean
    public WxMaService wxMaService() {
        WxMaServiceClusterImpl service = new WxMaServiceClusterImpl();
        service.setWxMaConfig(wxMaInRedisConfig);
        service.setRedissonClient(redissonClient);
        return service;
    }


}
