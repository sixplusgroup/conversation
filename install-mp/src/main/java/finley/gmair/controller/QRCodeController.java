package finley.gmair.controller;

import finley.gmair.service.QRCodeService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: QRCodeController
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/18 4:10 PM
 */
@RestController
@RequestMapping("/install-mp/qrcode")
public class QRCodeController {
    @Autowired
    private QRCodeService qrCodeService;

    /**
     * 安装工人扫码，根据二维码的URL解析实际的二维码值
     *
     * @param url
     * @return
     */
    @PostMapping("/decode")
    public ResultData decode(String url) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(url)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供设备的二维码信息");
            return result;
        }
        result = qrCodeService.decodeURL(url);
        return result;
    }
}
