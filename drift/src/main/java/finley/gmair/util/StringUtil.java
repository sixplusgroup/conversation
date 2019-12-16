package finley.gmair.util;

import org.springframework.util.StringUtils;

/**
 * @ClassName: StringUtil
 * @Description: TODO
 * @Author fan
 * @Date 2019/7/15 11:08 AM
 */
public class StringUtil {
    public static boolean isEmpty(String... args) {
        for (String item : args) {
            if (StringUtils.isEmpty(item)) return true;
        }
        return false;
    }

    public static String toMessage(String[] modify,String expressNum,String company,String machineOrderNo,String description,String username){
        String message = username+"通过上传excel更新了订单:";
        if(modify[2]!=null&&!modify[2].equals("")){
            message += "快递单号："+expressNum+"、";
        }
        if(modify[3]!=null&&!modify[3].equals("")){
            message += "快递公司："+company+"、";
        }
        if(modify[0]!=null&&!modify[0].equals("")){
            message += "机器码："+machineOrderNo+"、";
        }
        if(modify[1]!=null&&!modify[1].equals("")){
            message += "备注："+description+"、";
        }
        return message;
    }
}
