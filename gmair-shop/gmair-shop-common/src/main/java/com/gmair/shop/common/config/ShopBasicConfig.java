

package com.gmair.shop.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


/**
 * 商城配置文件
 *
 */
@Data
@Component
@PropertySource("classpath:shop.properties")
@ConfigurationProperties(prefix = "shop")
public class ShopBasicConfig {


	/**
	 * 用于加解密token的密钥
	 */
	private String tokenAesKey;

}
