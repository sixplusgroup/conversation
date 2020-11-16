package finley.gmair.service.impl;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.OrderMapper;
import finley.gmair.dao.SkuItemMapper;
import finley.gmair.dao.TradeMapper;
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

import javax.annotation.Resource;
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

   /* @Resource
    private CrmLocalAPIService crmLocalAPIService;*/

    @Resource
    private TradeMapper tradeMapper;

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

        // 如果状态不是TRADE_CLOSED禁止推送到CRM
        if (!TbTradeStatus.valueOf(interTrade.getStatus()).judgeCrmUpdate()) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("交易状态错误");
            return res;
        }

        List<Order> orders = orderMapper.selectAllByTradeId(interTrade.getTradeId());
        for (Order tmpOrder : orders) {
            // 甲醛检测仪租赁和检测试纸不同步到CRM
            if (DRIFT_NUM_IID.equals(tmpOrder.getNumIid())) continue;
            CrmStatusDTO newCrmStatus = new CrmStatusDTO();
            // （子订单）订单号：
            String ddh = interTrade.getTid().equals(tmpOrder.getOid()) ? String.valueOf(tmpOrder.getOid()) :
                    String.valueOf(interTrade.getTid()) +"-"+ String.valueOf(tmpOrder.getOid());
            newCrmStatus.setDdh(ddh);
            // 联系方式：
            newCrmStatus.setLxfs(interTrade.getReceiverMobile());
            // 根据实物和虚拟订单选择不同的订单状态转换策略
            CrmOrderStatus billStatus = selectStatusTransStrategy(tmpOrder);
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

        // 如果状态是 TRADE_CLOSED_BY_TAOBAO 或者是 WAIT_BUYER_PAY 都禁止推送到CRM
        if (!TbTradeStatus.valueOf(interTrade.getStatus()).judgeCrmAdd()) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("交易状态错误");
            return res;
        }
        List<Order> orders = orderMapper.selectAllByTradeId(interTrade.getTradeId());
        // 判断是否是多子订单交易
        boolean isMultiOrders = orders.size() > 1;
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
            String ddh = interTrade.getTid().equals(tmpOrder.getOid()) ? String.valueOf(tmpOrder.getOid()) :
                    interTrade.getTid() + "-" + tmpOrder.getOid();
            newCrmOrder.setDdh(ddh);
            // 数量
            newCrmOrder.setSl(String.valueOf(tmpOrder.getNum()));
            // 实收金额
            Double ssje;
            if (tmpOrder.getDivideOrderFee() != null) {
                ssje = tmpOrder.getDivideOrderFee();
            }else{
                if (isMultiOrders && tmpOrder.getPartMjzDiscount() != null){
                    // 多子订单且part_mjz_discount（优惠分摊）不为空
                    ssje = tmpOrder.getPayment() - tmpOrder.getPartMjzDiscount();
                }else{
                    ssje = tmpOrder.getPayment();
                }
            }
            newCrmOrder.setSsje(String.valueOf(ssje));
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
            // 买家留言
            String buyerMes = interTrade.getBuyerMessage();
            if (buyerMes == null) {
                newCrmOrder.setBuyermes("");
            } else {
                newCrmOrder.setBuyermes(buyerMes);
            }
            // 卖家备注
            String sellerMemo = interTrade.getSellerMemo();
            if (sellerMemo == null) {
                newCrmOrder.setSellermes("");
            } else {
                newCrmOrder.setSellermes(sellerMemo);
            }
            // 根据实物和虚拟订单选择不同的订单状态转换策略
            CrmOrderStatus billStatus = selectStatusTransStrategy(tmpOrder);
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
     * @param order 中台订单
     * @return 返回订单中商品的机器型号，如不存在则返回“该机器型号未录入”
     * @author zm
     * @date 2020/11/02 14:29
     * @description 查询机器型号
     **/
    private String getMachineModel(Order order) {
        List<String> machineModelList;
        // 部分订单存在sku_id缺省，如果根据num_id贺sku_id查不到就仅根据num_id查询
        if (order.getSkuId() == null) {
            machineModelList = skuItemMapper.selectMachineModelByNumIid(
                    String.valueOf(order.getNumIid()));
        } else {
            machineModelList = skuItemMapper.selectMachineModelByNumIidAndSkuId(
                    String.valueOf(order.getNumIid()),
                    String.valueOf(order.getSkuId()));
        }

        if (machineModelList == null || machineModelList.size() == 0) {
            return "该机器型号未录入";
        } else {
            return machineModelList.get(0);
        }
    }

    /**
     * @param order 中台订单
     * @return 是虚拟订单则返回true
     * @author zm
     * @date 2020/11/05 20:20
     * @description 判断是不是虚拟订单
     */
    private boolean isVirtualOrder(Order order) {
        List<Boolean> resList;
        // 部分订单存在sku_id缺省，如果根据num_id贺sku_id查不到就仅根据num_id查询
        if (order.getSkuId() == null) {
            resList = skuItemMapper.selectFictitiousByNumIid(
                    String.valueOf(order.getNumIid()));
        } else {
            resList = skuItemMapper.selectFictitiousByNumIidAndSkuId(
                    String.valueOf(order.getNumIid()),
                    String.valueOf(order.getSkuId()));
        }

        if (resList == null || resList.size() == 0) {
            return false;
        } else {
            return resList.get(0);
        }
    }

    /**
     * @param interOrder 中台订单
     * @return 返回根据策略转换后的Crm订单状态
     * @author zm
     * @date 2020/11/10 10:26
     * @description 根据实物或者虚拟订单选择不同订单状态转换策略
     **/
    private CrmOrderStatus selectStatusTransStrategy(Order interOrder) {
        TbStatusTransStrategy transStrategy;
        // 判断是否为虚拟订单：
        if (isVirtualOrder(interOrder)) {
            transStrategy = new VirtualOrderTrans();
        } else {
            transStrategy = new PhysicalOrderTrans();
        }
        return transStrategy.transTbOrderStatus(interOrder);
    }
}