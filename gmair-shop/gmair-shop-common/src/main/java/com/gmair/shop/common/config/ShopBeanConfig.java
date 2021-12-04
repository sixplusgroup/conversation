

package com.gmair.shop.common.config;

import cn.hutool.crypto.symmetric.AES;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class ShopBeanConfig {

	private final ShopBasicConfig shopBasicConfig;


    @Bean
    public AES tokenAes() {
    	return new AES(shopBasicConfig.getTokenAesKey().getBytes());
    }

}
