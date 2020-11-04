package finley.gmair.service.impl;

import finley.gmair.dao.OrderMapper;
import finley.gmair.dao.SkuItemMapper;
import finley.gmair.dao.TradeMapper;
import finley.gmair.model.dto.TbOrderExcel;
import finley.gmair.model.ordernew.Order;
import finley.gmair.model.ordernew.TbTradeStatus;
import finley.gmair.model.ordernew.Trade;
import finley.gmair.model.ordernew.TradeFrom;
import finley.gmair.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：tsl
 * @date ：Created in 2020/11/2 14:13
 * @description ：
 */

@Service
public class TradeServiceImpl implements TradeService {
    @Autowired
    TradeMapper tradeMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    SkuItemMapper skuItemMapper;

    @Override
    public List<Trade> selectAll() {
        return tradeMapper.selectAll();
    }

    @Override
    public List<TbOrderExcel> selectAllTradeExcel() {
        List<Order> orderList = orderMapper.selectAll();
        List<TbOrderExcel> excelList = orderList.stream().map(order -> {
            Trade trade = tradeMapper.selectByPrimaryKey(order.getTradeId());
            String numIid = order.getNumIid() == null ? "" : order.getNumIid().toString();
            String skuId = order.getSkuId() == null ? "" : order.getSkuId().toString();
            List<String> modelList = skuItemMapper.selectMachineModelByNumIidAndSkuId(numIid, skuId);
            if (CollectionUtils.isEmpty(modelList)) {
                modelList = skuItemMapper.selectMachineModelByNumIid(numIid);
            }
            String machineModel = CollectionUtils.isEmpty(modelList) ? "" : modelList.get(0);
            TbOrderExcel excel = new TbOrderExcel();
            excel.setTradeFrom(TradeFrom.TMALL.getDesc());
            excel.setTid(trade.getTid().toString());
            excel.setMachineModel(machineModel);
            excel.setPropertyName(order.getSkuPropertiesName());
            excel.setNum(order.getNum());
            excel.setPayment(order.getPayment());
            excel.setCreated(trade.getCreated());
            excel.setStatus(TbTradeStatus.valueOf(order.getStatus()).getDesc());
            excel.setReceiverName(trade.getReceiverName());
            excel.setReceiverMobile(trade.getReceiverMobile());
            excel.setReceiverCity(trade.getReceiverCity());
            excel.setReceiverAddress(trade.getReceiverState() + trade.getReceiverCity()
                    + trade.getReceiverDistrict() + trade.getReceiverAddress());
            return excel;
        }).sorted((o1, o2) -> {
            if (o1.getCreated().before(o2.getCreated())) {
                return -1;
            } else if (o1.getCreated().after(o2.getCreated())) {
                return 1;
            }
            return 0;
        }).collect(Collectors.toList());
        return excelList;
    }

}
