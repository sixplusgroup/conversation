package finley.gmair.service.impl;

import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.OrderMapper;
import finley.gmair.dao.SkuItemMapper;
import finley.gmair.model.dto.CrmOrderDTO;
import finley.gmair.model.dto.CrmStatusDTO;
import finley.gmair.model.ordernew.*;
import finley.gmair.service.CrmAPIService;
import finley.gmair.service.CrmSyncService;
import finley.gmair.service.strategy.TbStatusTransStrategy;
import finley.gmair.service.strategy.impl.PhysicalOrderTrans;
import finley.gmair.service.strategy.impl.VirtualOrderTrans;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author zm
 * @date 2020/11/01 12:35 下午
 * @description 中台订单同步到CRM系统中
 */
@Service
public class CrmSyncServiceImpl implements CrmSyncService {
    private static final Long DRIFT_NUM_IID = 618391118089L;
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

        // 只有mode==2（交易之前已推给CRM新增过）才能更新状态并推给CRM
        if (interTrade.getMode() != TradeMode.PUSHED_TO_CRM.getValue()) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("交易模糊字段状态错误");
            return res;
        }
        List<Order> orders = orderMapper.selectAllByTradeId(interTrade.getTradeId());
        for (Order tmpOrder : orders) {
            // 甲醛检测仪租赁和检测试纸不同步到CRM
            if (DRIFT_NUM_IID.equals(tmpOrder.getNumIid())) continue;
            CrmStatusDTO newCrmStatus = new CrmStatusDTO();
            // （子订单）订单号：
            newCrmStatus.setDdh(String.valueOf(tmpOrder.getOid()));
            // 联系方式：
            newCrmStatus.setLxfs(interTrade.getReceiverMobile());
            // 根据实物和虚拟订单选择不同的订单状态转换策略
            TbStatusTransStrategy strategy;
            if (isVirtualOrder(tmpOrder)) strategy = new VirtualOrderTrans();
            else strategy = new PhysicalOrderTrans();
            // 订单状态：
            CrmOrderStatus billStatus = strategy.transTbOrderStatus(tmpOrder);
            if (billStatus == null) {
                res.setResponseCode(ResponseCode.RESPONSE_ERROR);
                res.setDescription("交易状态转换失败");
                return res;
            }
            newCrmStatus.setBillstat(String.valueOf(billStatus.getValue()));
            JSONObject ans = crmAPIService.updateOrderStatus(
                    JSONObject.toJSON(newCrmStatus).toString());
            if (Objects.equals(ans.get("ResponseCode").toString(),
                    ResponseCode.RESPONSE_ERROR.toString())) {
                res.setResponseCode(ResponseCode.RESPONSE_ERROR);
                res.setDescription("更新交易状态到CRM失败");
                return res;
            }
        }
        res.setResponseCode(ResponseCode.RESPONSE_OK);
        res.setDescription("交易同步到CRM成功");
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultData createNewOrder(Trade interTrade) {
        ResultData res = new ResultData();

        // 只有去模糊化的交易mode==1才同步
        if (interTrade.getMode() != TradeMode.DEBLUR.getValue()) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("交易模糊字段状态错误");
            return res;
        }

        // 如果状态是TRADE_CLOSED_BY_TAOBAO或者是WAIT_BUYER_PAY都禁止推送到CRM
        if (!TbTradeStatus.valueOf(interTrade.getStatus()).judgeCrmAdd()) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("交易状态错误");
            return res;
        }
        List<Order> orders = orderMapper.selectAllByTradeId(interTrade.getTradeId());
        for (Order tmpOrder : orders) {
            // 甲醛检测仪租赁和检测试纸不同步到CRM
            if (DRIFT_NUM_IID.equals(tmpOrder.getNumIid())) continue;
            CrmOrderDTO newCrmOrder = new CrmOrderDTO();
            // 渠道来源
            newCrmOrder.setQdly("58");
            // 机器型号（根据sku_id和num_iid去获取）
            String machineModel = getMachineModel(tmpOrder);
            // 属性名称
            String skuPropertyName = tmpOrder.getSkuPropertiesName();
            // 拼接型号和属性名称
            String property = skuPropertyName != null && skuPropertyName.length() > 5 ? skuPropertyName.substring(5) : "";
            newCrmOrder.setJqxh(machineModel + property);
            // 订单号
            newCrmOrder.setDdh(String.valueOf(tmpOrder.getOid()));
            // 数量
            newCrmOrder.setSl(String.valueOf(tmpOrder.getNum()));
            // 实收金额
            newCrmOrder.setSsje(String.valueOf(tmpOrder.getPayment()));
            // 付款日期（格式为：年-月-日）
            newCrmOrder.setXdrq(interTrade.getPayTimeStr());
            // 用户姓名
            newCrmOrder.setYhxm(interTrade.getReceiverName());
            // 联系方式
            newCrmOrder.setLxfs(interTrade.getReceiverMobile());
            // 地区（例如：杭州市、南京市等）
            newCrmOrder.setDq(interTrade.getReceiverCity());
            // 地址
            newCrmOrder.setDz(interTrade.getReceiverAddress());
            // 根据实物和虚拟订单选择不同的订单状态转换策略
            TbStatusTransStrategy strategy;
            if (isVirtualOrder(tmpOrder)) strategy = new VirtualOrderTrans();
            else strategy = new PhysicalOrderTrans();
            // 订单状态
            CrmOrderStatus billStatus = strategy.transTbOrderStatus(tmpOrder);
            if (billStatus == null) {
                res.setResponseCode(ResponseCode.RESPONSE_ERROR);
                res.setDescription("交易状态转换失败");
                return res;
            }
            newCrmOrder.setBillstat(String.valueOf(billStatus.getValue()));
            JSONObject ans = crmAPIService.createNewOrder(
                    JSONObject.toJSON(newCrmOrder).toString());
            if (Objects.equals(ans.get("ResponseCode").toString(),
                    ResponseCode.RESPONSE_ERROR.toString())) {
                res.setResponseCode(ResponseCode.RESPONSE_ERROR);
                res.setDescription(ans.get("Description").toString());
                return res;
            }
        }
        res.setResponseCode(ResponseCode.RESPONSE_OK);
        res.setDescription("新增交易到CRM成功");
        return res;
    }

    /**
     * @param order finley.gmair.model.ordernew.Order
     * @author zm
     * @date 2020/11/02 14:29
     * @description 查询机器型号
     **/
    private String getMachineModel(Order order) {
        String numId = String.valueOf(order.getNumIid());
        String skuId = String.valueOf(order.getSkuId());
        List<String> machineModelList;
        // 部分订单存在sku_id缺省，如果根据num_id贺sku_id查不到就仅根据num_id查询
        if (skuId == null || skuId.equals("")) {
            machineModelList = skuItemMapper.selectMachineModelByNumIid(numId);
        } else {
            machineModelList = skuItemMapper.selectMachineModelByNumIidAndSkuId(numId, skuId);
        }

        if (machineModelList == null || machineModelList.size() == 0) {
            return "该机器型号未录入";
        } else {
            return machineModelList.get(0);
        }
    }

    /**
     * @param order finley.gmair.model.ordernew.Order
     * @author zm
     * @date 2020/11/05 20:20
     * @description 判断是不是虚拟订单，是则返回true
     */
    private boolean isVirtualOrder(Order order) {
        String numId = String.valueOf(order.getNumIid());
        String skuId = String.valueOf(order.getSkuId());
        List<Boolean> resList;
        // 部分订单存在sku_id缺省，如果根据num_id贺sku_id查不到就仅根据num_id查询
        if (skuId == null || skuId.equals("")) {
            resList = skuItemMapper.selectFictitiousByNumIid(numId);
        } else {
            resList = skuItemMapper.selectFictitiousByNumIidAndSkuId(numId, skuId);
        }

        if (resList == null || resList.size() == 0) {
            return false;
        } else {
            return resList.get(0);
        }
    }
}