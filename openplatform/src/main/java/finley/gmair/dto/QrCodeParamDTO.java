package finley.gmair.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @author lyy
 * @Description
 * @create 2020-07-03 11:43 下午
 */
public class QrCodeParamDTO {
    String appid;

    @JSONField(name = "qrcode_list")
    List<String> qrCodeList;

    public String getAppid() {
        return appid;
    }

    public List<String> getQrCodeList() {
        return qrCodeList;
    }
}
