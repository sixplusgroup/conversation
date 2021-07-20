package finley.gmair.dto;

import java.util.List;

/**
 * @author lyy
 * @date 2020-07-03 11:43 下午
 */
public class QrCodeParamDTO {
    String appid;

    List<String> qrCodeList;

    public String getAppid() {
        return appid;
    }

    public List<String> getQrCodeList() {
        return qrCodeList;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setQrCodeList(List<String> qrCodeList) {
        this.qrCodeList = qrCodeList;
    }
}
