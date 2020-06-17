package finley.gmair.controller;

import finley.gmair.model.bill.BillInfo;
import finley.gmair.model.bill.BillStatus;
import finley.gmair.model.bill.DealSnapshot;
import finley.gmair.service.BillService;
import finley.gmair.service.DriftService;
import finley.gmair.service.SnapshotService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.wepayUtil.XMLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bill/info")
public class BillController {

    @Autowired
    private BillService billService;

    @Autowired
    private DriftService driftService;

    @Autowired
    private SnapshotService snapshotService;


    @PostMapping(value = "/create")
    public ResultData createBill(String orderId,  double actualPrice) {
        ResultData result = new ResultData();
        BillInfo billInfo = new BillInfo(orderId, actualPrice);
        ResultData response = billService.createBill(billInfo);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("error");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }

        return result;
    }

    @PostMapping(value = "/delete")
    public ResultData deleteBill(String billId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(billId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("error");
            return result;
        }

        ResultData response = billService.deleteBill(billId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to delete bill");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("Succeed to delete bill");
        }
        return result;
    }

    @GetMapping(value = "/query")
    public ResultData getBillInfo() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = billService.fetchBill(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No bill found");
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Query error,try again later");
                break;
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                break;
        }
        return result;
    }

    @PostMapping(value = "/buy")
    @ResponseBody
    public ResultData Buy(HttpServletRequest request, HttpServletResponse response) throws Exception {
            ResultData result = new ResultData();
            BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine()) != null){
                sb.append(line);
            }
            br.close();
            //sb为微信返回的xml
            String notifyxml = sb.toString();
            String resxml = "";
            Map map = XMLUtil.doXMLParse(notifyxml);
            String returncode = (String) map.get("return_code");
            if("SUCCESS".equals(returncode)) {// 推送支付结果  --成功
                String bill_id = (String) map.get("out_trade_no");
                String app_id = (String)map.get("appid");
                String account_id = (String)map.get("openid");
                String mch_id = (String)map.get("mch_id");
                String device_info = (String) map.get("device_info");
                String is_subscribe = (String) map.get("is_subscribe");
                String channel_id = (String) map.get("trade_type");
                String bank_type = (String) map.get("bank_type");
                double order_price = (double) map.get("total_fee");
                double actual_price = (double) map.get("settlement_total_fee");
                String fee_type = (String) map.get("fee_type");
                String transaction_id = (String) map.get("transaction_id");
                String time_end = (String) map.get("time_end");

                resxml = "<xml>" + "<return_code>![CDATA[SUCCESS]]></return_code>"+"<return_msg>![CDATA[OK]]</return_msg></xml>";

                //找到相应bill
                Map<String, Object> condition = new HashMap<>();
                condition.put("billId", bill_id);
                ResultData rs = billService.fetchBill(condition);
                if (result.getResponseCode() != ResponseCode.RESPONSE_OK){
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription(new StringBuffer("error").toString());
                    return result;
                }
                result.setResponseCode(ResponseCode.RESPONSE_OK);

                //更改订单状态
                BillInfo bill = ((List<BillInfo>) rs.getData()).get(0);
                bill.setStatus(BillStatus.PAYED);
                String orderId = bill.getOrderId();
                billService.updateBill(bill);

                //创建Snapshot并传数据
                DealSnapshot snapshot = new DealSnapshot(actual_price, bill_id, channel_id, app_id, account_id,
                        mch_id, device_info, is_subscribe, bank_type, order_price,
                        fee_type, transaction_id, time_end);
                snapshotService.createSnapshot(snapshot);

                //通知driftOrder更改order状态
                new Thread(() -> {
                    driftService.orderPayed(orderId);
                }).start();
                result.setDescription("Bill is already payed");

            }else {
                resxml = "<xml>" + "<return_code>![CDATA[FAIL]]></return_code>"+"<return_msg>![CDATA[报文为空]]</return_msg></xml>";
            }
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(resxml.getBytes());
            out.flush();
            out.close();
            return  result;
        }
    }
