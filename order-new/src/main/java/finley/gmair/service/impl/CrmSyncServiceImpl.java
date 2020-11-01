package finley.gmair.service.impl;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.OrderMapper;
import finley.gmair.dao.SkuItemMapper;
import finley.gmair.model.dto.CrmOrderDTO;
import finley.gmair.model.dto.CrmStatusDTO;
import finley.gmair.model.ordernew.Order;
import finley.gmair.model.ordernew.TbTradeStatus;
import finley.gmair.model.ordernew.Trade;
import finley.gmair.service.CrmAPIService;
import finley.gmair.service.CrmSyncService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zm
 * @date 2020/11/01 12:35 下午
 * @description 中台订单同步到CRM系统中
 */
@Service
public class CrmSyncServiceImpl implements CrmSyncService {

    @Autowired
    private SkuItemMapper skuItemMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CrmAPIService crmAPIService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultData updateOrderStatus(Trade interTrade) {
        ResultData res = new ResultData();

        List<Order> orders = orderMapper.selectAllByTradeId(interTrade.getTradeId());
        for (Order tmpOrder : orders) {
            CrmStatusDTO newCrmStatus = new CrmStatusDTO();
            // （子订单）订单号：
            newCrmStatus.setDdh(String.valueOf(tmpOrder.getOid()));
            // 联系方式：
            newCrmStatus.setLxfs(interTrade.getReceiverMobile());
            // 订单状态：
            newCrmStatus.setBillstat(String.valueOf(TbTradeStatus.valueOf(
                    tmpOrder.getStatus()).toCrmOrderStatus().getValue()));

            JSONObject ans = crmAPIService.updateOrderStatus(
                    JSONObject.toJSON(newCrmStatus).toString());
            if (ans.get("ResponseCode") == ResponseCode.RESPONSE_ERROR) {
                res.setResponseCode(ResponseCode.RESPONSE_ERROR);
                res.setDescription("更新交易状态到中台失败");
                return res;
            }
        }
        res.setResponseCode(ResponseCode.RESPONSE_OK);
        res.setDescription("交易同步到中台成功");
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultData createNewOrder(Trade interTrade) {
        ResultData res = new ResultData();

        List<Order> orders = orderMapper.selectAllByTradeId(interTrade.getTradeId());
        for (Order tmpOrder : orders) {
            CrmOrderDTO newCrmOrder = new CrmOrderDTO();
            // 渠道来源
            newCrmOrder.setQdly("58");
            // TODO 机器型号（加一个字段）
            newCrmOrder.setJqxh("");
            // 订单号
            newCrmOrder.setDdh(String.valueOf(tmpOrder.getOid()));
            // 数量
            newCrmOrder.setSl(String.valueOf(tmpOrder.getNum()));
            // 下单日期
            newCrmOrder.setXdrq(String.valueOf(interTrade.getCreated()));
            // 实收金额
            newCrmOrder.setSsje(String.valueOf(tmpOrder.getPayment()));
            // 用户姓名
            newCrmOrder.setYhxm(interTrade.getReceiverName());
            // 联系方式
            newCrmOrder.setLxfs(interTrade.getReceiverMobile());
            // 地区
            newCrmOrder.setDq(interTrade.getReceiverDistrict());
            // 地址
            newCrmOrder.setDz(interTrade.getReceiverAddress());
            // 订单状态
            newCrmOrder.setBillstat(TbTradeStatus.valueOf(
                    tmpOrder.getStatus()).toCrmOrderStatus().name());
            JSONObject ans = crmAPIService.createNewOrder(
                    JSONObject.toJSON(newCrmOrder).toString());
            if (ans.get("ResponseCode") == ResponseCode.RESPONSE_ERROR) {
                res.setResponseCode(ResponseCode.RESPONSE_ERROR);
                res.setDescription("新增交易到中台失败");
                return res;
            }
        }
        res.setResponseCode(ResponseCode.RESPONSE_OK);
        res.setDescription("新增交易到中台成功");
        return res;
    }
}