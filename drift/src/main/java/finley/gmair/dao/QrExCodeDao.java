package finley.gmair.dao;

import finley.gmair.model.drift.QR_EXcode;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface QrExCodeDao {
    ResultData query(Map<String, Object> condition);

    ResultData insert(QR_EXcode code);
}
