package finley.gmair.service;

import finley.gmair.model.bill.BillInfo;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface BillService {

    ResultData fetchBill(Map<String, Object> condition);

    ResultData createBill(BillInfo billInfo);

    ResultData updateBill(BillInfo billInfo);

    ResultData deleteBill(String billId);

}
