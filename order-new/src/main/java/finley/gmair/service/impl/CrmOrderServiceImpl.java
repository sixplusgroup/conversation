package finley.gmair.service.impl;

import finley.gmair.model.dto.CrmOrderDTO;
import finley.gmair.model.ordernew.Order;
import finley.gmair.model.ordernew.Trade;
import finley.gmair.service.CrmOrderService;
import finley.gmair.util.ResultData;

/**
 * @author zm
 * @date 2020/10/26 0026 17:49
 * @description TODO
 **/
public class CrmOrderServiceImpl implements CrmOrderService {
    /**
     * 将中台系统中的trade（一个父订单）转存到crm系统中
     *
     * @param trade
     * @return finley.gmair.util.ResultData
     * @author zm
     * @date 2020/10/26 0026 10:42
     **/
    @Override
    public ResultData createTrade(Trade trade) {
        ResultData result = new ResultData();
        return result;
    }

    /**
     * 将中台系统中的单个order（一个子订单）转存到crm系统中
     *
     * @param order
     * @return finley.gmair.util.ResultData
     * @author zm
     * @date 2020/10/26 0026 10:42
     **/
    @Override
    public ResultData createOrder(Order order) {
        CrmOrderDTO newOrder = new CrmOrderDTO();

        ResultData result = new ResultData();
        return result;
    }
}
