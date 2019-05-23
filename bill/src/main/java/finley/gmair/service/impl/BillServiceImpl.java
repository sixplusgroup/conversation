package finley.gmair.service.impl;

import finley.gmair.dao.BillDao;
import finley.gmair.model.bill.BillInfo;
import finley.gmair.service.BillService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillDao billDao;

    @Override
    public ResultData fetchBill(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = billDao.query(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No bill found");
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to query bill");
                break;
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                break;
        }
        return result;

    }

    @Override
    public ResultData createBill(BillInfo billInfo) {
        ResultData result = new ResultData();
        ResultData response = billDao.insert(billInfo);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert bill with: " + billInfo.toString());
            return result;
        }
        String billId = ((BillInfo) response.getData()).getBillId();
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData updateBill(BillInfo billInfo) {
        ResultData result = new ResultData();
        ResultData response = billDao.update(billInfo);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to update bill with billId: ")
                    .append(billInfo.getBillId()).append(" to ").append(billInfo.toString()).toString());
        }
        return result;

    }

    @Override
    public ResultData deleteBill(String billId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("billId",billId);
        ResultData response = billDao.query(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query bill");
            return result;
        }
        result = billDao.delete(billId);
        return result;
    }
}
