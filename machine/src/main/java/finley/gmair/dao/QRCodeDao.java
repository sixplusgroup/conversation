package finley.gmair.dao;

import finley.gmair.model.machine.QRCode;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/5/28
 */
public interface QRCodeDao {
    ResultData insert(QRCode code);

    ResultData query(Map<String, Object> condition);

    ResultData queryBatch(Map<String, Object> condition);

    ResultData updateByQRcode(Map<String, Object> condition);
}
