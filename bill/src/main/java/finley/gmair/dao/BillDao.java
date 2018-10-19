package finley.gmair.dao;

import finley.gmair.model.bill.BillInfo;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface BillDao {
    ResultData query(Map<String, Object> condition);

    ResultData insert(BillInfo billInfo);

    ResultData update(BillInfo billInfo);
}
