package finley.gmair.repo;

import finley.gmair.converter.UnifiedOrderDataConverter;
import finley.gmair.converter.UnifiedTradeDataConverter;
import finley.gmair.dao.UnifiedOrderDOMapper;
import finley.gmair.dao.UnifiedTradeDOMapper;
import finley.gmair.model.domain.UnifiedOrder;
import finley.gmair.model.domain.UnifiedTrade;
import finley.gmair.model.entity.UnifiedOrderDO;
import finley.gmair.model.entity.UnifiedTradeDO;
import finley.gmair.model.request.TradeQuery;
import finley.gmair.util.IDGenerator;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-18 20:58
 * @description ：
 */

@Repository
public class UnifiedTradeRepo {
    @Resource
    UnifiedTradeDOMapper unifiedTradeDOMapper;

    @Resource
    UnifiedOrderDOMapper unifiedOrderDOMapper;

    @Resource
    UnifiedTradeDataConverter unifiedTradeDataConverter;

    @Resource
    UnifiedOrderDataConverter unifiedOrderDataConverter;

    public List<UnifiedTrade> query(TradeQuery query) {
        List<UnifiedTradeDO> tradeDOList = unifiedTradeDOMapper.query(query);
        List<UnifiedTrade> tradeList = unifiedTradeDataConverter.fromDataList(tradeDOList);
        tradeList.forEach(trade -> {
            List<UnifiedOrder> unifiedOrderList = findUnifiedOrderListByTradeId(trade.getTradeId());
            trade.setOrderList(unifiedOrderList);
        });
        return tradeList;
    }

    /**
     * 根据主键id查询主订单聚合(包括子订单)
     *
     * @param id 主键
     * @return UnifiedTrade 主订单聚合
     */
    public UnifiedTrade findById(String id) {
        UnifiedTradeDO unifiedTradeDO = unifiedTradeDOMapper.selectByPrimaryKey(id);
        if (unifiedTradeDO == null) {
            return null;
        }
        UnifiedTrade unifiedTrade = unifiedTradeDataConverter.fromData(unifiedTradeDO);
        List<UnifiedOrder> unifiedOrderList = findUnifiedOrderListByTradeId(unifiedTradeDO.getTradeId());
        unifiedTrade.setOrderList(unifiedOrderList);
        return unifiedTrade;
    }

    /**
     * 根据业务订单号+订单平台查询主订单聚合(包括子订单)
     *
     * @param tid      业务订单号
     * @param platform 平台
     * @return UnifiedTrade 主订单聚合
     */
    public UnifiedTrade findByTid(String tid, int platform) {
        UnifiedTradeDO unifiedTradeDO = unifiedTradeDOMapper.selectByTidAndPlatform(tid, platform);
        if (unifiedTradeDO == null) {
            return null;
        }
        UnifiedTrade unifiedTrade = unifiedTradeDataConverter.fromData(unifiedTradeDO);
        List<UnifiedOrder> unifiedOrderList = findUnifiedOrderListByTradeId(unifiedTradeDO.getTradeId());
        unifiedTrade.setOrderList(unifiedOrderList);
        return unifiedTrade;
    }

    /**
     * 根据业务订单号+店铺Id查询主订单聚合(包括子订单)
     *
     * @param tid      业务订单号
     * @param shopId   店铺Id
     * @return UnifiedTrade 主订单聚合
     */
    public UnifiedTrade findByTid(String tid, String shopId) {
        UnifiedTradeDO unifiedTradeDO = unifiedTradeDOMapper.selectByTidAndShopId(tid, shopId);
        if (unifiedTradeDO == null) {
            return null;
        }
        UnifiedTrade unifiedTrade = unifiedTradeDataConverter.fromData(unifiedTradeDO);
        List<UnifiedOrder> unifiedOrderList = findUnifiedOrderListByTradeId(unifiedTradeDO.getTradeId());
        unifiedTrade.setOrderList(unifiedOrderList);
        return unifiedTrade;
    }

    /**
     * 保存主订单聚合（包括子订单）
     *
     * @param unifiedTrade 主订单聚合
     */
    public void save(UnifiedTrade unifiedTrade) {
        if (unifiedTrade.getTradeId() != null) {
            // 更新(主订单更新,子订单也只能更新不能插入)
            UnifiedTradeDO unifiedTradeDO = unifiedTradeDataConverter.toData(unifiedTrade);
            unifiedTradeDOMapper.updateByPrimaryKeySelective(unifiedTradeDO);
            if (unifiedTrade.getOrderList() != null) {
                for (UnifiedOrder unifiedOrder : unifiedTrade.getOrderList()) {
                    UnifiedOrderDO unifiedOrderDO = unifiedOrderDataConverter.toData(unifiedOrder);
                    unifiedOrderDOMapper.updateByPrimaryKeySelective(unifiedOrderDO);
                }
            }
        } else {
            // 插入(主订单插入,子订单也只能插入不能更新)
            unifiedTrade.setTradeId(IDGenerator.generate("TRA"));
            for (UnifiedOrder unifiedOrder : unifiedTrade.getOrderList()) {
                unifiedOrder.setOrderId(IDGenerator.generate("ORD"));
                unifiedOrder.setTradeId(unifiedTrade.getTradeId());
            }
            UnifiedTradeDO unifiedTradeDO = unifiedTradeDataConverter.toData(unifiedTrade);
            unifiedTradeDOMapper.insertSelective(unifiedTradeDO);
            if (unifiedTrade.getOrderList() != null) {
                for (UnifiedOrder unifiedOrder : unifiedTrade.getOrderList()) {
                    unifiedOrder.setTradeId(unifiedTradeDO.getTradeId());
                    UnifiedOrderDO unifiedOrderDO = unifiedOrderDataConverter.toData(unifiedOrder);
                    unifiedOrderDOMapper.insertSelective(unifiedOrderDO);
                }
            }
        }
    }

    /**
     * 根据tid和platform查询,如果存在填充tradeId和orderId
     * 填充is_fuzzy,consumerInfo防止更新重新模糊化
     *
     * @param unifiedTrade 主订单聚合
     */
    public void fillPrimaryKey(UnifiedTrade unifiedTrade) {
        UnifiedTrade tradeInDb = findByTid(unifiedTrade.getTid(), unifiedTrade.getTradePlatform());
        if (null != tradeInDb) {
            //主订单已存在,根据tid设置主订单和子订单主键
            unifiedTrade.setTradeId(tradeInDb.getTradeId());
            unifiedTrade.setIsFuzzy(tradeInDb.getIsFuzzy());
            unifiedTrade.setConsigneeName(tradeInDb.getConsigneeName());
            unifiedTrade.setConsigneeProvince(tradeInDb.getConsigneeProvince());
            unifiedTrade.setConsigneeCity(tradeInDb.getConsigneeCity());
            unifiedTrade.setConsigneeDistrict(tradeInDb.getConsigneeDistrict());
            unifiedTrade.setConsigneeAddress(tradeInDb.getConsigneeAddress());
            unifiedTrade.setConsigneePhone(tradeInDb.getConsigneePhone());
            unifiedTrade.setCrmPushStatus(tradeInDb.getCrmPushStatus());
            unifiedTrade.setDriftPushStatus(tradeInDb.getDriftPushStatus());
            List<UnifiedOrder> orderListInDb = tradeInDb.getOrderList();
            Map<String, UnifiedOrder> oidOrderMap = orderListInDb.stream().collect(
                    Collectors.toMap(UnifiedOrder::getOid, Function.identity()));
            unifiedTrade.getOrderList().forEach(unifiedOrder -> {
                unifiedOrder.setOrderId(oidOrderMap.get(unifiedOrder.getOid()).getOrderId());
            });
        }
    }


    /**
     * 单独更新主订单信息（不包括子订单）
     *
     * @param unifiedTrade
     */
    public void updateTradeOnly(UnifiedTrade unifiedTrade) {
        if (unifiedTrade.getTradeId() == null) {
            throw new IllegalArgumentException("cannot update consigneeInfo with null tradeId");
        }
        UnifiedTradeDO unifiedTradeDO = unifiedTradeDataConverter.toData(unifiedTrade);
        unifiedTradeDOMapper.updateByPrimaryKeySelective(unifiedTradeDO);
    }

    private List<UnifiedOrder> findUnifiedOrderListByTradeId(String tradeId) {
        List<UnifiedOrderDO> unifiedOrderDOList = unifiedOrderDOMapper.selectByTradeId(tradeId);
        return unifiedOrderDataConverter.fromDataList(unifiedOrderDOList);
    }
}
