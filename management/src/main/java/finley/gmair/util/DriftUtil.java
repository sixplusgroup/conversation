package finley.gmair.util;

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
}
