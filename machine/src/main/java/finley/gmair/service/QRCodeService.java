package finley.gmair.service;

import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/5/28
 */
public interface QRCodeService {
    ResultData create(String goodsId, String modelId, String batchValue, int num);

    ResultData fetch(Map<String, Object> condition);

    ResultData fetchBatch(Map<String, Object> condition);

    ResultData modifyByQRcode(Map<String, Object> condition);

    ResultData profile(String qrcode);
}
