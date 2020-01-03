package finley.gmair.util;

import finley.gmair.model.drift.DriftOrderStatus;
import finley.gmair.model.drift.EXCodeStatus;
import org.springframework.util.StringUtils;

public class DriftUtil {

    public static String setStatus(int status){
        String result = "";
        switch (status) {
            case 0:
                result = "已申请";
                break;
            case 1:
                result = "已支付";
                break;
            case 2:
                result = "已确认";
                break;
            case 3:
                result = "已发货";
                break;
            case 4:
                result = "已寄回";
                break;
            case 5:
                result = "已完成";
                break;
            case 6:
                result = "已关闭";
                break;
            case 7:
                result = "已取消";
                break;
        }
        return result;
    }

    public static String setExcodeStatus(String status){
        String result = "";
        switch (status) {
            case "CREATED":
                result = "已创建";
                break;
            case "EXCHANGED":
                result = "可兑换";
                break;
            case "OCCUPIED":
                result = "已使用";
                break;
        }
        return result;
    }

    public static String updateMessage(String consignee,String phone,String province,String city,String district,String address,String status,String expectedDate){
        String result = "";
        if(!StringUtils.isEmpty(consignee)){
            result+="用户为"+consignee+"、";
        }
        if(!StringUtils.isEmpty(phone)){
            result+="联系方式为"+phone+"、";
        }
        if(!StringUtils.isEmpty(province)){
            result+="地址为"+province+city+district+address+"、";
        }
        if(!StringUtils.isEmpty(expectedDate)){
            result+="使用日期为"+expectedDate+"、";
        }
        if(!StringUtils.isEmpty(status)){
            result+="订单状态为"+ status +"。";
        }
        return result;
    }
}
