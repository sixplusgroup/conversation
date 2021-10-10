package com.gmair.shop.service.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author Joby
 */
@Component
public class PayClientFeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("payClient","SHOPMP");
    }
}
