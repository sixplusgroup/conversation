package finley.gmair.service.impl;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.ConfigurationDao;
import finley.gmair.dao.ReturnInfoDao;
import finley.gmair.dao.TradeDao;
import finley.gmair.model.payment.Configuration;
import finley.gmair.model.payment.ReturnInfo;
import finley.gmair.model.payment.TradeState;
import finley.gmair.service.WechatService;
import finley.gmair.model.payment.Trade;
import finley.gmair.service.feign.OrderService;
import finley.gmair.util.*;

import java.sql.Timestamp;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * @ClassName: WechatService
 * @Description: TODO
 * @Author fan
 * @Date 2019/7/23 4:28 PM
 */
@Service
@PropertySource({"classpath:wechatpay.properties"})
public class WechatServiceImpl implements WechatService {

    private static Logger logger = Logger.getLogger(WechatServiceImpl.class);

    @Value("${app_id}")
    private String appId;

    @Value("${merchant_id}")
    private String merchantId;

    @Value("${key}")
    private String key;

    private String environment;
    private String sandboxKey;

    @Autowired
    private TradeDao tradeDao;

    @Autowired
    private ConfigurationDao configurationDao;

    @Autowired
    private ReturnInfoDao returnInfoDao;

    @Autowired
    private OrderService orderService;

    @Override
    public ResultData payCreate(String orderId, String openId, String money, String ipAddress, String body) {

        ResultData result = new ResultData();

        String payUrl = null;

        ResultData configData = configurationDao.query();
        if(configData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            Configuration config = ((List<Configuration>)configData.getData()).get(0);
            environment = config.getEnvironment();
            payUrl = config.getPayUrl();
        }

        String tradeId = PayUtil.generateId();

        //必传参数
        String nonce_str=UUID.randomUUID().toString().replace("-", "");
        //String body="果麦新风-甲醛检测";
        String spbill_create_ip=ipAddress;
        String notify_url="https://microservice.gmair.net/payment/bill/notify";
        String trade_type="JSAPI";//小程序
        String total_fee=money;//订单总结额，单位为分
        String out_trade_no=orderId;//订单号
        //非必传参数

        //判断订单是否重入
        boolean orderExisted = checkOrderExisted(orderId);
        if(orderExisted) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("重复提交订单");
            return result;
        }

        //组装参数map
        try {

            //沙盒测试获取key
            if(!environment.equals("actual")) {
                Map<String,String> paramMap=new TreeMap<>();
                paramMap.put("mch_id", merchantId);
                paramMap.put("nonce_str", nonce_str);
                paramMap.put("sign_type","MD5");
                String tempsign = PayUtil.generateSignature(paramMap,key);
                paramMap.put("sign", tempsign);
                String paramXml=PayUtil.mapToXml(paramMap);
                String returnStr = PayUtil.httpRequest("https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey", "POST", paramXml);
                Map<String, String> respMap = XMLUtil.doXMLParse(returnStr);
                if ("SUCCESS".equals(respMap.get("return_code"))) {
                    sandboxKey = respMap.get("sandbox_signkey");
                    logger.info("sandbox key：" + sandboxKey);
                }
            }


            Map<String,String> paramMap=new TreeMap<>();
            paramMap.put("appid", appId);
            paramMap.put("body", body);
            paramMap.put("mch_id", merchantId);
            paramMap.put("nonce_str", nonce_str);
            paramMap.put("sign_type","MD5");
            paramMap.put("out_trade_no", out_trade_no);
            paramMap.put("total_fee", total_fee);
            paramMap.put("spbill_create_ip", spbill_create_ip);
            paramMap.put("notify_url", notify_url);
            paramMap.put("trade_type", trade_type);
            paramMap.put("openid", openId);
            String sign = PayUtil.generateSignature(paramMap,environment.equals("actual")?key:sandboxKey);
            paramMap.put("sign", sign);

            //转换 xml
            String paramXml=PayUtil.mapToXml(paramMap);
            logger.info("send xml: " + paramXml);

            //发送请求
            String resultXml =PayUtil.httpRequest(payUrl, "POST", paramXml);
            logger.info("tryNum:0, result:"+resultXml);

            int tryCount = 0;
            while(tryCount < 5 && resultXml.contains("<title>302")) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tryCount++;
                resultXml =PayUtil.httpRequest(payUrl, "POST", paramXml);
                logger.info("tryNum:" + tryCount + ", result:"+resultXml);
            }
            if(resultXml.contains("<title>302")) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("302 error");
                return result;
            }

            //解析响应xml
            Map<String, String> respMap = XMLUtil.doXMLParse(resultXml);

            Trade trade=new Trade();
            trade.setTradeId(tradeId);
            trade.setOrderId(orderId);
            trade.setTradeDescription(body);
            trade.setTradeNonceStr(nonce_str);
            trade.setTradeTotalFee(Integer.parseInt(total_fee));
            trade.setTradeSpbillCreateIp(spbill_create_ip);
            trade.setTradeType(trade_type);
            trade.setTradeOpenId(openId);
            trade.setTradeStartTime(new Timestamp(System.currentTimeMillis()));
            trade.setTradeState(TradeState.UNPAYED);

            String return_code = respMap.get("return_code");
            if ("SUCCESS".equals(return_code)) {
                if (respMap.containsKey("err_code_des") && !respMap.get("err_code_des").toLowerCase().equals("ok")
                        && !respMap.get("err_code_des").toUpperCase().equals("SUCCESS")) {
                    logger.error("err_code_des:"+respMap.get("err_code_des"));
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription(respMap.get("err_code_des"));
                    return result;
                }
                String result_code = respMap.get("result_code");
                if ("SUCCESS".equals(result_code)) {//业务结果
                    tradeDao.insert(trade);

                    //存储微信返回的map
                    ReturnInfo info = new ReturnInfo();
                    info.setInfoId(PayUtil.generateId());
                    info.setOrderId(orderId);
                    info.setDeviceInfo(respMap.get("device_info"));
                    info.setNonceStr(respMap.get("nonce_str"));
                    info.setPrepayId(respMap.get("prepay_id"));
                    info.setSign(respMap.get("sign"));
                    info.setTradeType(respMap.get("trade_type"));
                    returnInfoDao.insert(info);

                    //返回的map
                    result.setResponseCode(ResponseCode.RESPONSE_OK);
                    result.setData(respMap);
                    return result;
                }
            } else {
                logger.error("return_msg:" + respMap.get("return_msg"));
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(respMap.get("return_msg"));
                return result;
            }

        } catch (Exception e) {
            logger.error("error message:"+e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }
        logger.error("咋执行到这了，好奇怪");
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("null error");
        return result;
    }

    @Override
    public String payNotify(String notifyXml) {
        logger.info("notify String: " + notifyXml);
        try {
            Map<String, String> notifyMap = XMLUtil.doXMLParse(notifyXml);
            //判断该通知是否已经处理过

            if ("FAIL".equals(notifyMap.get("return_code"))) {
                Map<String,String> paraMap=new HashMap<>();
                paraMap.put("return_code", notifyMap.get("return_code"));
                paraMap.put("return_msg", notifyMap.get("return_msg"));
                logger.error("支付失败:"+PayUtil.mapToXml(paraMap));
                return PayUtil.mapToXml(paraMap);
            }
            if ("FAIL".equals(notifyMap.get("result_code"))) {
                Map<String,String> paraMap=new HashMap<>();
                paraMap.put("return_code", notifyMap.get("err_code"));
                paraMap.put("return_msg", notifyMap.get("err_code_des"));
                logger.error("支付失败:"+PayUtil.mapToXml(paraMap));
                return PayUtil.mapToXml(paraMap);
            }
            if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                String out_trade_no = notifyMap.get("out_trade_no");//没有订单号这个返回值，要修改
                Map<String, Object> queryMap = new HashMap<>();
                queryMap.put("orderId", out_trade_no);
                ResultData tradeResult = tradeDao.query(queryMap);
                if(tradeResult.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                    logger.error(tradeResult.getDescription());
                }
                if(tradeResult.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                    logger.error("orderId query null !");
                }
                Trade trade = ((List<Trade>)tradeResult.getData()).get(0);
                if (trade!=null) {
                    //签名验证,并校验返回的订单金额是否与商户侧的订单金额一致
                    Map<String, String> signMap=notifyMap;
                    String sign = PayUtil.generateSignature(signMap, key) ;//重新签名
                    if (StringUtils.isNotBlank(sign)&&sign.equals(notifyMap.get("sign"))) {
                        //金额验证
                        int total_fee = trade.getTradeTotalFee();
                        int total_fee_resp = Integer.parseInt(notifyMap.get("total_fee"));
                        if (total_fee==total_fee_resp) {
                            trade.setTradeEndTime(new Timestamp(System.currentTimeMillis()));
                            //支付完成,更新订单状态
                            trade.setTradeState(TradeState.PAYED);

                            //调用并更改订单的状态
                            orderService.updateOrderPayed(trade.getOrderId());
                            logger.info("order state updated!");

                            tradeDao.update(trade);
                            logger.info("trade state updated!");

                            //返回给微信
                            Map<String,String> paraMap=new HashMap<>();
                            paraMap.put("return_code", "SUCCESS");
                            paraMap.put("return_msg",notifyMap.get("return_msg"));
                            String wxXml = PayUtil.mapToXml(paraMap);
                            return wxXml;
                        }
                    }else {
                        logger.error("sign签名验证失败");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("error message:"+e.getMessage());
            return null;
        }
        logger.error("咋执行到这了，try catch 都没return");
        return null;
    }

    private boolean checkOrderExisted(String orderId) {
        ResultData result = tradeDao.existOrder(orderId);
        if(result.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            logger.error("checkOrderExisted出错");
            return true;
        }

        boolean exist = true;
        if(result.getResponseCode() == ResponseCode.RESPONSE_OK) {
            exist = (Boolean)result.getData();
        }
        return exist;
    }

    public ResultData getTradeByOrderId(String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        return tradeDao.query(map);
    }

    public ResultData getCreateResult(String orderId) {

        ResultData result = new ResultData();

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        ResultData returnData = returnInfoDao.query(map);
        if(returnData.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(returnData.getDescription());
        } else if(returnData.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("order id does not exist !");
        } else {
            ReturnInfo info = ((List<ReturnInfo>)returnData.getData()).get(0);

            Timestamp timeStamp = info.getCreateAt();
            String nonceStr = info.getNonceStr();
            String packageStr = "prepay_id=" + info.getPrepayId();
            String signType = "MD5";

            Map<String,String> paramMap=new TreeMap<>();
            paramMap.put("appId", appId);
            paramMap.put("timeStamp", timeStamp.getTime()/1000+ "");
            paramMap.put("nonceStr", nonceStr);
            paramMap.put("package", packageStr);
            paramMap.put("signType", signType);
            String paySign = PayUtil.generateSignature(paramMap,environment.equals("actual")?key:sandboxKey);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("timeStamp", timeStamp.getTime()/1000+ "");
            jsonObject.put("nonceStr", nonceStr);
            jsonObject.put("package", packageStr);
            jsonObject.put("signType", signType);
            jsonObject.put("paySign", paySign);

            result.setData(jsonObject);
        }
        return result;
    }

    public ResultData checkTradePayed() {
        ResultData result = new ResultData();
        // 查询数据库获取所有满足条件的未支付状态的订单编号
        ResultData orderResult = tradeDao.queryUnpayed();
        if(orderResult.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(orderResult.getDescription());
            logger.error("query error:" + orderResult.getDescription());
            return result;
        }
        List<Trade> tradeList = null;
        if(orderResult.getResponseCode() == ResponseCode.RESPONSE_OK) {
            tradeList = (List<Trade>)orderResult.getData();
        }

        String queryUrl = "https://api.mch.weixin.qq.com/sandboxnew/pay/orderquery";
        ResultData configData = configurationDao.query();
        if(configData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            Configuration config = ((List<Configuration>)configData.getData()).get(0);
            environment = config.getEnvironment();
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(orderResult.getDescription());
            logger.error(orderResult.getDescription());
            return result;
        }
        //沙盒测试获取key
        if(!environment.equals("actual")) {
            Map<String,String> paramMap=new TreeMap<>();
            paramMap.put("mch_id", merchantId);
            paramMap.put("nonce_str", UUID.randomUUID().toString().replace("-", ""));
            paramMap.put("sign_type","MD5");
            String tempsign = PayUtil.generateSignature(paramMap,key);
            paramMap.put("sign", tempsign);
            String paramXml=PayUtil.mapToXml(paramMap);
            String returnStr = PayUtil.httpRequest("https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey", "POST", paramXml);
            Map<String, String> respMap = XMLUtil.doXMLParse(returnStr);
            if ("SUCCESS".equals(respMap.get("return_code"))) {
                sandboxKey = respMap.get("sandbox_signkey");
                logger.info("sandbox key：" + sandboxKey);
            }
        }
        //遍历逐个查询当前账单的支付状态，若发现账单已支付，则更新账单状态，并通知drift模块进行更新
        for(Trade trade : tradeList) {
            String orderId = trade.getOrderId();
            logger.info("orderid : " + orderId);

            Map<String,String> paramMap=new TreeMap<>();
            paramMap.put("appid", appId);
            paramMap.put("mch_id", merchantId);
            paramMap.put("out_trade_no", orderId);
            paramMap.put("nonce_str", UUID.randomUUID().toString().replace("-", ""));
            paramMap.put("sign_type", "MD5");
            String sign = PayUtil.generateSignature(paramMap,environment.equals("actual")?key:sandboxKey);
            paramMap.put("sign", sign);

            //转换 xml
            String paramXml=PayUtil.mapToXml(paramMap);
            logger.info("send xml: " + paramXml);

            //发送请求
            String resultXml =PayUtil.httpRequest(queryUrl, "POST", paramXml);
            logger.info("tryNum:0, result:"+resultXml);
            int tryCount = 0;
            while(tryCount < 5 && resultXml.contains("<title>302")) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tryCount++;
                resultXml =PayUtil.httpRequest(queryUrl, "POST", paramXml);
                logger.info("tryNum:" + tryCount + ", result:"+resultXml);
            }
            if(resultXml.contains("<title>302")) {
                logger.error("302 error: try 5 times fail");
                continue;
//                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
//                result.setDescription("302 error");
//                return result;
            }

            //解析响应xml
            Map<String, String> respMap = XMLUtil.doXMLParse(resultXml);

            String return_code = respMap.get("return_code");
            if ("SUCCESS".equals(return_code)) {
                if (respMap.containsKey("err_code_des") && !respMap.get("err_code_des").toLowerCase().equals("ok")
                        && !respMap.get("err_code_des").toUpperCase().equals("SUCCESS")) {
                    logger.error("err_code_des:" + respMap.get("err_code_des"));
                    continue;
//                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
//                    result.setDescription(respMap.get("err_code_des"));
//                    return result;
                }
                String result_code = respMap.get("result_code");
                if ("SUCCESS".equals(result_code)) {//业务结果
                    String trade_state = respMap.get("trade_state");
                    if(trade_state.equals("SUCCESS")) {
System.out.println("trade openid:" + trade.getTradeOpenId());
System.out.println("return openid:" + respMap.get("openid"));
System.out.println("orderId:" + trade.getOrderId());
System.out.println("out_trade_no:" + respMap.get("out_trade_no"));
                        if(trade.getTradeTotalFee() == Integer.parseInt(respMap.get("total_fee"))
                            && trade.getTradeOpenId().equals(respMap.get("openid"))
                            && trade.getOrderId().equals(respMap.get("out_trade_no"))) {
                            trade.setTradeState(TradeState.PAYED);
                            trade.setTradeEndTime(new Timestamp(Long.parseLong(respMap.get("time_end")) * 1000));
                            orderService.updateOrderPayed(trade.getOrderId());
                            logger.info("order state updated!");

                            tradeDao.update(trade);
                            logger.info("trade state updated!");
                        }
                    } else {
                        logger.info("state of orderId ( " + orderId + ") : " + trade_state);
                    }
                }
            } else {
                logger.error("return_msg:" + respMap.get("return_msg"));
                continue;
//                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
//                result.setDescription(respMap.get("return_msg"));
//                return result;
            }
        }
        return result;
    }

}
