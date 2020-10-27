package finley.gmair.service.impl;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.OrderMapper;
import finley.gmair.model.dto.CrmOrderDTO;
import finley.gmair.model.ordernew.*;
import finley.gmair.service.CrmAPIService;
import finley.gmair.service.CrmOrderService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zm
 * @date 2020/10/26 0026 17:49
 **/
public class CrmOrderServiceImpl implements CrmOrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CrmAPIService crmAPIService;

    /**
     * 将中台系统中的trade（一个父订单）转存到crm系统中
     *
     * @param trade
     * @return finley.gmair.util.ResultData
     * @author zm
     * @date 2020/10/26 0026 10:42
     **/
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public ResultData createTrade(Trade trade) {
        ResultData result = new ResultData();
        // 订单日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tradeDate = sdf.format(trade.getCreated());
        // 收件人姓名
        String receiverName = trade.getReceiverName();
        // 收件人手机号
        String phoneNum = trade.getReceiverMobile();
        // 详细地址
        String dz = trade.getReceiverState() + " " + trade.getReceiverCity() + " " + trade.getReceiverDistrict() + ""
                + trade.getReceiverTown() + "" + trade.getReceiverAddress();
        // 地区
        String dq = trade.getReceiverCity();

        List<Order> orderList = orderMapper.selectAllByTradeId(trade.getTradeId());

        for (Order tmpOrder :
                orderList) {
            CrmOrderDTO newOrder = new CrmOrderDTO();
            // 设置渠道来源
            newOrder.setQdly(TradeSource.TMALL.getValue());
            // TODO 机器型号（因为淘宝来的数据没有一个字段就是表示机器型号，需要从sku_properties_name或者title去提取）
            newOrder.setJqxh("GM420");
            // TODO 订单号是order中的num_iid还是o_id还是怎么表示
            newOrder.setDdh("123456789");
            newOrder.setSl(String.valueOf(tmpOrder.getNum()));
            newOrder.setXdrq(tradeDate);
            // TODO ssje是实售金额还是实收金额
            newOrder.setSsje(String.valueOf(tmpOrder.getTotalFee()));
            newOrder.setYhxm(receiverName);
            newOrder.setLxfs(phoneNum);
            newOrder.setDq(dq);
            newOrder.setDz(dz);

            String billState = String.valueOf(
                    transTbTradeStat(TbTradeStatus.valueOf(tmpOrder.getStatus())).getValue());
            newOrder.setBillstat(billState);
            String strOrder = JSONObject.toJSON(newOrder).toString();
            JSONObject response = crmAPIService.createNewOrder(strOrder);
            if (response.get("ResponseCode").equals(ResponseCode.RESPONSE_ERROR.toString())) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(response.get("Description").toString());
                return result;
            }
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("向crm系统中新增交易成功");
        return result;
    }

    @Override
    public CrmOrderStatus transTbTradeStat(TbTradeStatus tbTradeStatus) {
        CrmOrderStatus crmRes;
        switch (tbTradeStatus) {
            case TRADE_CLOSED_BY_TAOBAO:
            case WAIT_BUYER_PAY:
            case WAIT_SELLER_SEND_GOODS:
                // 未处理的初始状态
                crmRes = CrmOrderStatus.UNTREATED;
                break;
            case SELLER_CONSIGNED_PART:
            case WAIT_BUYER_CONFIRM_GOODS:
                // 部分发货、等待买家确认收货 -> 已发货运输中
                crmRes = CrmOrderStatus.DELIVERED_IN_TRANSIT;
                break;
            case TRADE_CLOSED:
                // 交易关闭（退款流程的最终状态）
                crmRes = CrmOrderStatus.GOODS_RETURNED;
                break;
            case TRADE_FINISHED:
                // 交易完成（正常购物流程的最终状态）
                crmRes = CrmOrderStatus.ALL_INSTALLATION_COMPLETED;
                break;
            default:
                crmRes = null;
        }
        return crmRes;
    }

    @Override
    public TbTradeStatus transCrmOrderStat(CrmOrderStatus crmOrderStatus) {
        TbTradeStatus tbRes;
        switch (crmOrderStatus) {
            case UNTREATED:
            case PRE_SURVEY_CONTACT_FAILED:
            case UNPAID_REMOTEFEE:
            case CONSIDER_WHETHER_INSTALL:
            case WAITING_SCHEDULED:
            case SURVEYED_REQUEST_DELIVERY_DELAY:
            case SURVEYED_NO_DELIVERY:
            case CONTACTED_WAITING_NOTIFICATION:
            case GLASS_SURVEYED:
            case SCHEDULING:
            case NOT_IN_INSTALLATION_AREA:
                // 均为等待卖家发货阶段
                tbRes = TbTradeStatus.WAIT_SELLER_SEND_GOODS;
                break;

            case SCHEDULE_INSTALLATION_CONTACT_FAILED:
            case DELIVERED_IN_TRANSIT:
            case DELIVERED_NO_SCHEDULED:
            case DELIVERED_OF_ACCESSORIES_BOX:
            case PRIORITY_DELIVERED_BY_ACCESSORIES_BOX:
                // 等待买家确认收货
                tbRes = TbTradeStatus.WAIT_BUYER_CONFIRM_GOODS;
                break;
            case PARTIAL_INSTALLATION_COMPLETED:
            case ALL_INSTALLATION_COMPLETED:
            case READY_TO_RETURN:
                // 已收货
                tbRes = TbTradeStatus.TRADE_FINISHED;
                break;
            case GOODS_RETURNED:
                tbRes = TbTradeStatus.TRADE_CLOSED;
                break;
            default:
                tbRes = null;
        }
        return null;
    }
}
