package finley.gmair.service.impl;

import finley.gmair.dao.BillDao;
import finley.gmair.model.bill.BillInfo;
import finley.gmair.service.BillService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    private BillDao billDao;
    @Override
    public ResultData fetchBill(Map<String, Object> condition) {
        return null;
    }

    @Override
    public ResultData createBill(BillInfo billInfo) {
        return billDao.insert(billInfo);
    }

    @Override
    public ResultData updateBill(BillInfo billInfo) {
        return null;
    }

    @Override
    public ResultData deleteBill(String billId) {
        return null;
    }
}
