package finley.gmair.converter;

import finley.gmair.model.domain.UnifiedOrder;
import finley.gmair.model.domain.UnifiedSkuItem;
import finley.gmair.model.domain.UnifiedTrade;
import finley.gmair.model.dto.OrderExcel;
import finley.gmair.repo.UnifiedSkuItemRepo;
import org.apache.commons.compress.utils.Lists;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-26 12:46
 * @description ：
 */

@Mapper(componentModel = "spring")
public abstract class OrderExcelConverter {

    @Resource
    UnifiedSkuItemRepo unifiedSkuItemRepo;

    @Mapping(target = "platform", expression = "java(finley.gmair.model.enums.PlatformEnum." +
            "getEnumByValue(unifiedTrade.getTradePlatform()).getDesc())")
    @Mapping(target = "channel", source = "skuItem.channel")
    @Mapping(target = "tid", source = "unifiedTrade.tid")
    @Mapping(target = "machineModel", source = "skuItem.machineModel")
    @Mapping(target = "propertiesName", source = "skuItem.propertiesName")
    @Mapping(target = "num", source = "unifiedOrder.num")
    @Mapping(target = "payment", source = "unifiedOrder.payment")
    @Mapping(target = "payTime", source = "unifiedTrade.payTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "status", expression = "java(finley.gmair.model.enums.OrderStatusEnum." +
            "getEnumByValue(unifiedOrder.getStatus()).getDesc())")
    //consignee* 字段无冲突省略
    public abstract OrderExcel toExcel(UnifiedTrade unifiedTrade, UnifiedOrder unifiedOrder, UnifiedSkuItem skuItem);

    public List<OrderExcel> toExcelList(UnifiedTrade unifiedTrade) {
        if (unifiedTrade == null || CollectionUtils.isEmpty(unifiedTrade.getOrderList())) {
            return Lists.newArrayList();
        }
        return unifiedTrade.getOrderList().stream().map(order -> {
            UnifiedSkuItem skuItem = unifiedSkuItemRepo.findByShopAndSku(
                    unifiedTrade.getShopId(), order.getSkuId(), order.getNumId());
            return toExcel(unifiedTrade, order, skuItem);
        }).collect(Collectors.toList());
    }

    public List<OrderExcel> toExcelList(List<UnifiedTrade> tradeList) {
        if (CollectionUtils.isEmpty(tradeList)) {
            return Lists.newArrayList();
        }
        List<OrderExcel> result = Lists.newArrayList();
        tradeList.forEach(trade -> result.addAll(toExcelList(trade)));
        return result;
    }
}
