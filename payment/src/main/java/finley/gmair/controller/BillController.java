package finley.gmair.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import finley.gmair.service.WechatService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: PaymentController
 * @Description: TODO
 * @Author fan
 * @Date 2019/7/23 4:25 PM
 */
@RestController
@RequestMapping("/payment/bill")
public class BillController {

    @Autowired
    WechatService wechatService;

    /**
     * @param orderId 订单号
     * @param openid  用户openid
     * @param price   交易金额，单位：分
     * @param body    格式：商家名称-销售商品类目
     * @param ip      终端ip，调用微信支付API的机器IP
     * @return
     */
    @PostMapping("/create")
    public ResultData createTrade(String orderId, String openid, int price, String body, String ip, @RequestHeader(defaultValue = "OFFICIALACCOUNT") String payClient) {

        return wechatService.payCreate(orderId, openid, price + "", ip, body,payClient);
    }

    /**
     * 微信支付回调方法
     *
     * @param
     * @return
     */
    @PostMapping("/notify")
    @ResponseBody
    public void notified(@RequestHeader(defaultValue = "OFFICIALACCOUNT") String payClient,HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String notityXml = parseRequst(request);
            String responseXml = wechatService.payNotify(notityXml,payClient);
            writer.write(responseXml);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
        //return wechatService.payNotify(xml);
    }

    private static String parseRequst(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        String body = "";
        try {
            ServletInputStream inputStream = request.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8")); // 设置编码格式“utf-8”否则获取中文为乱码
            while (true) {
                String info = br.readLine();
                if (info == null) {
                    break;
                }
                if (body == null || "".equals(body)) {
                    body = info;
                } else {
                    body += info;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }

    /**
     * 根据订单号获得账单信息
     *
     * @param orderId 订单号
     * @return
     */
    @GetMapping("/getTrade")
    public ResultData getTrade(String orderId,@RequestHeader(defaultValue = "OFFICIALACCOUNT") String payClient) {
        return wechatService.getTradeByOrderId(orderId,payClient);
    }

    /**
     * 根据订单号获取创建微信支付请求的结果
     *
     * @param orderId
     * @return
     */
    @GetMapping("/getCreateResult")
    public ResultData getCreateResult(String orderId,@RequestHeader(defaultValue = "OFFICIALACCOUNT") String payClient) {
        return wechatService.getCreateResult(orderId,payClient);
    }

    /**
     * 该接口负责主动发起账单支付状态的查询
     *
     * @return
     */
    @PostMapping("/sync")
    public ResultData sync(@RequestHeader(defaultValue = "OFFICIALACCOUNT") String payClient) {
        // 查询数据库获取所有满足条件的未支付状态的订单编号

        //遍历逐个查询当前账单的支付状态，若发现账单已支付，则更新账单状态，并通知drift模块进行更新
        return wechatService.checkTradePayed(payClient);
    }

}
