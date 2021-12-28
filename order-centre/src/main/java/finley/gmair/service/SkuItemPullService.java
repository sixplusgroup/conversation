package finley.gmair.service;

import finley.gmair.handler.SkuItemPullHandlerContext;
import finley.gmair.model.result.PullResult;
import finley.gmair.model.domain.UnifiedShop;
import finley.gmair.repo.UnifiedShopRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-24 0:11
 * @description ：
 */

@Service
public class SkuItemPullService {

    private Logger logger = LoggerFactory.getLogger(SkuItemPullService.class);

    @Resource
    UnifiedShopRepo unifiedShopRepo;

    @Resource
    SkuItemPullHandlerContext handlerContext;

    public void pull(String shopId) {
        UnifiedShop shop = unifiedShopRepo.findById(shopId);
        PullResult pullResult = handlerContext.handlePull(shop);
        //todo:保存pullResult
    }
}
