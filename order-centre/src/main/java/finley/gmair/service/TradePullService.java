package finley.gmair.service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import finley.gmair.handler.TradePullHandlerContext;
import finley.gmair.model.domain.UnifiedShop;
import finley.gmair.model.request.BatchPullRequest;
import finley.gmair.model.request.PullRequestDTO;
import finley.gmair.model.request.SinglePullRequest;
import finley.gmair.model.result.PullResult;
import finley.gmair.repo.UnifiedShopRepo;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/17 15:38
 * @description ：
 */

@Service
public class TradePullService {

    @Resource
    UnifiedShopRepo unifiedShopRepo;

    @Resource
    TradePullHandlerContext handlerContext;

    private Logger logger = LoggerFactory.getLogger(TradePullService.class);

    public void singlePull(String shopId, String tid) {
        UnifiedShop shop = unifiedShopRepo.findById(shopId);
        Preconditions.checkArgument(shop != null, "不存在该店铺");
        SinglePullRequest request = new SinglePullRequest(shopId, tid);
        PullResult pullResult = handlerContext.handleSinglePull(request, shop);
        //todo:保存pullResult
    }

    public void batchPull(String shopId, PullRequestDTO requestDTO) {
        UnifiedShop shop = unifiedShopRepo.findById(shopId);
        Preconditions.checkArgument(shop != null, "不存在该店铺");
        BatchPullRequest request = BatchPullRequest.buildByShopAndModified(shop, requestDTO);
        PullResult pullResult = handlerContext.handleBatchPull(request, shop);
        //todo:保存pullResult
    }

    public void schedulePull(String shopId) {
        ResultData resultData = new ResultData();
        UnifiedShop shop = unifiedShopRepo.findById(shopId);
        Preconditions.checkArgument(shop != null, "不存在该店铺");
        logger.info("schedulePull, shop:{}", JSON.toJSONString(shop));
        schedulePullByShop(shop);
    }

    public void schedulePullAll() {
        ResultData resultData = new ResultData();
        List<UnifiedShop> shopList = unifiedShopRepo.findAll();
        Preconditions.checkArgument(!CollectionUtils.isEmpty(shopList), "不存在店铺");
        logger.info("schedulePullAll, shopList:{}", JSON.toJSONString(shopList));
        for (UnifiedShop shop : shopList) {
            schedulePullByShop(shop);
        }
    }

    private void schedulePullByShop(UnifiedShop shop) {
        BatchPullRequest request = BatchPullRequest.buildByShopSchedule(shop);
        try {
            PullResult pullResult = handlerContext.handleBatchPull(request, shop);
        } catch (Exception e) {
            logger.error("schedulePull error,shop:{},request:{}", JSON.toJSONString(shop), JSON.toJSONString(request), e);
        }
        //更新卖家用户信息
        shop.updateLastPullTime(request.getEndTime());
        unifiedShopRepo.updatePullInfo(shop);
        //todo:保存pullResult
    }
}
