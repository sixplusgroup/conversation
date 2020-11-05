package finley.gmair.service.impl;

import finley.gmair.dao.OrderMapper;
import finley.gmair.dao.TradeMapper;
import finley.gmair.model.dto.OrderInfo;
import finley.gmair.model.dto.TbOrderExcel;
import finley.gmair.model.ordernew.TbTradeStatus;
import finley.gmair.model.ordernew.Trade;
import finley.gmair.model.ordernew.TradeFrom;
import finley.gmair.service.SkuItemService;
import finley.gmair.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    SkuItemService skuItemService;

    @Override
    public List<Trade> selectAll() {
        return tradeMapper.selectAll();
    }

    @Override
    public List<TbOrderExcel> selectAllTradeExcel() {
        List<OrderInfo> orderList = orderMapper.selectOrderInfOrderByPayTime();

        return orderList.stream().map(o -> {
            TbOrderExcel excel = new TbOrderExcel();
            excel.setTradeFrom(TradeFrom.TMALL.getDesc());
            excel.setTid(o.getTid().toString());
            String skuPropertiesName = o.getSkuPropertiesName();
            String property = skuPropertiesName != null && skuPropertiesName.length() > 5 ? skuPropertiesName.substring(5) : "";
            String numIid = o.getNumIid() == null ? "" : o.getNumIid().toString();
            String skuId = o.getSkuId() == null ? "" : o.getSkuId().toString();
            String machineModel = skuItemService.selectMachineModelByNumIidAndSkuId(numIid, skuId);
            excel.setMachineModel(machineModel);
            excel.setPropertyName(property);
            excel.setNum(o.getNum());
            excel.setPayment(o.getPayment());
            excel.setCreated(o.getPayTime());
            excel.setStatus(TbTradeStatus.valueOf(o.getStatus()).getDesc());
            excel.setReceiverName(o.getReceiverName());
            excel.setReceiverMobile(o.getReceiverMobile());
            excel.setReceiverCity(o.getReceiverCity());
            excel.setReceiverAddress(o.getReceiverState() + o.getReceiverCity()
                    + o.getReceiverDistrict() + o.getReceiverAddress());
            return excel;
        }).collect(Collectors.toList());
    }

}
