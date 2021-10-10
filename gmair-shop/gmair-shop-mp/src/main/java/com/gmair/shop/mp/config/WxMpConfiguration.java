

package com.gmair.shop.mp.config;

import com.gmair.shop.mp.component.WxMpInRedisConfigStorage;
import com.gmair.shop.mp.component.WxMpServiceClusterImpl;
import com.gmair.shop.mp.handler.MenuHandler;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static me.chanjar.weixin.common.api.WxConsts.MenuButtonType;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * 微信公众号配置文件
 *
 */
@Configuration
@AllArgsConstructor
@ConditionalOnClass(WxMpService.class)
public class WxMpConfiguration {

    private final MenuHandler menuHandler;
    private final WxMpInRedisConfigStorage wxMpInRedisConfigStorage;
    private final RedissonClient redissonClient;

    @Bean
    public WxMpService wxMpService() {
        WxMpServiceClusterImpl service = new WxMpServiceClusterImpl();
        service.setWxMpConfigStorage(wxMpInRedisConfigStorage);
        service.setRedissonClient(redissonClient);
        return service;
    }

    @Bean
    public WxMpMessageRouter messageRouter() {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService());

        // 自定义菜单事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(MenuButtonType.CLICK).handler(this.menuHandler).end();


        return newRouter;
    }

}
