package finley.gmair.handler;

import finley.gmair.model.domain.ShopAuthorizeInfo;
import finley.gmair.model.domain.UnifiedShop;
import finley.gmair.model.domain.UnifiedSkuItem;
import finley.gmair.model.enums.PlatformEnum;
import finley.gmair.model.result.PullResult;
import finley.gmair.repo.UnifiedSkuItemRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-24 0:17
 * @description ：
 */

public abstract class AbstractSkuItemPullHandler<T> {
    @Resource
    UnifiedSkuItemRepo unifiedSkuItemRepo;

    private Logger logger = LoggerFactory.getLogger(AbstractSkuItemPullHandler.class);

    public PullResult handlePull(UnifiedShop unifiedShop) {
        logger.info("handlePull,shop:{}", unifiedShop);
        PullResult result = PullResult.buildBatchPullResult();
        List<T> originalSkuItemList = pull(unifiedShop.getShopAuthorizeInfo(), result);
        List<UnifiedSkuItem> unifiedSkuItemList = originalSkuItemList.stream().map(
                originalSkuItem -> {
                    UnifiedSkuItem skuItem = convert(originalSkuItem);
                    skuItem.setChannel(unifiedShop.getChannel());
                    skuItem.setShopId(unifiedShop.getShopId());
                    skuItem.setPlatform(unifiedShop.getPlatform());
                    return skuItem;
                }).collect(Collectors.toList());
        for (UnifiedSkuItem skuItem : unifiedSkuItemList) {
            unifiedSkuItemRepo.fillPrimaryKey(skuItem);
            unifiedSkuItemRepo.save(skuItem);
        }
        return result;
    }

    protected abstract List<T> pull(ShopAuthorizeInfo shopAuthorizeInfo, PullResult result);

    protected abstract UnifiedSkuItem convert(T originalSkuItem);

    protected abstract PlatformEnum getPlatform();
}
