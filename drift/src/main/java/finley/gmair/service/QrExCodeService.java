package finley.gmair.service;

import finley.gmair.model.drift.QR_EXcode;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface QrExCodeService {
    ResultData createQrExCode(QR_EXcode code);

    ResultData fetchQrExCode(Map<String, Object> condition);

    ResultData updateQrExCode(Map<String, Object> condition);
}
