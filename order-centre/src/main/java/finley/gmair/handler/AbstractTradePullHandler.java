package finley.gmair.handler;

import com.alibaba.fastjson.JSON;
import finley.gmair.model.domain.ShopAuthorizeInfo;
import finley.gmair.model.domain.TradeRecord;
import finley.gmair.model.domain.UnifiedShop;
import finley.gmair.model.domain.UnifiedTrade;
import finley.gmair.model.enums.PlatformEnum;
import finley.gmair.model.request.BatchPullRequest;
import finley.gmair.model.request.SinglePullRequest;
import finley.gmair.model.result.PullResult;
import finley.gmair.repo.TradeRecordRepo;
import finley.gmair.repo.UnifiedTradeRepo;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-21 20:20
 * @description ：
 */

public abstract class AbstractTradePullHandler<T> {
    @Resource
    UnifiedTradeRepo unifiedTradeRepo;

    @Resource
    TradeRecordRepo tradeRecordRepo;

    private Logger logger = LoggerFactory.getLogger(AbstractTradePullHandler.class);

    public PullResult handleSinglePull(SinglePullRequest request, UnifiedShop unifiedShop) {
        PullResult result = PullResult.buildSinglePullResult();
        T originalTrade = singlePull(request, unifiedShop.getShopAuthorizeInfo(), result);
        //todo:生成Record
        if (null != originalTrade) {
            TradeRecord tradeRecord = new TradeRecord();
            tradeRecord.setTid(getTid(originalTrade));
            tradeRecord.setPlatform(getPlatform().getValue());
            tradeRecord.setShopId(unifiedShop.getShopId());
            tradeRecord.setRecordMessage("同步一条订单");
            tradeRecord.setTradeData(JSON.toJSONString(originalTrade));
            tradeRecordRepo.add(tradeRecord);
            logger.info("pull new trade,platform:{},shopId:{},tid:{}", tradeRecord.getPlatform(), unifiedShop.getShopId(), tradeRecord.getTid());

            UnifiedTrade trade = convert(originalTrade);
            trade.setShopId(request.getShopId());
            unifiedTradeRepo.fillPrimaryKey(trade);
            unifiedTradeRepo.save(trade);
        }
        return result;
    }

    public PullResult handleBatchPull(BatchPullRequest request, UnifiedShop unifiedShop) {
        PullResult result = PullResult.buildBatchPullResult();
        List<T> originalTradeList = batchPull(request, unifiedShop.getShopAuthorizeInfo(), result);
        //todo:生成Record
        //todo:线程池
        if (CollectionUtils.isNotEmpty(originalTradeList)) {
            //todo:开始时间限制
            originalTradeList.forEach(trade -> {
                TradeRecord tradeRecord = new TradeRecord();
                tradeRecord.setTid(getTid(trade));
                tradeRecord.setPlatform(getPlatform().getValue());
                tradeRecord.setShopId(unifiedShop.getShopId());
                tradeRecord.setRecordMessage("同步一条订单");
                tradeRecord.setTradeData(JSON.toJSONString(trade));
                tradeRecordRepo.add(tradeRecord);
                logger.info("pull new trade,platform:{},shopId:{},tid:{}", tradeRecord.getPlatform(), unifiedShop.getShopId(), tradeRecord.getTid());
            });

            List<UnifiedTrade> unifiedTradeList = originalTradeList.stream()
                    .map(originalTrade -> {
                        UnifiedTrade trade = convert(originalTrade);
                        trade.setShopId(unifiedShop.getShopId());
                        return trade;
                    }).collect(Collectors.toList());
            unifiedTradeList.forEach(trade -> {
                unifiedTradeRepo.fillPrimaryKey(trade);
                unifiedTradeRepo.save(trade);
            });
        }
        return result;
    }


    protected abstract T singlePull(SinglePullRequest request, ShopAuthorizeInfo authorizeInfo, PullResult result);

    protected abstract List<T> batchPull(BatchPullRequest request, ShopAuthorizeInfo authorizeInfo, PullResult result);

    protected abstract UnifiedTrade convert(T originalTrade);

    protected abstract String getTid(T originalTrade);

    protected abstract PlatformEnum getPlatform();
}
