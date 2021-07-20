package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.BillDao;
import finley.gmair.model.bill.BillInfo;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class BillDaoImpl extends BaseDao implements BillDao {

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try{
            List<BillInfo> list = sqlSession.selectList("gmair.bill.info.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insert(BillInfo billInfo) {
        ResultData result = new ResultData();
        billInfo.setBillId(IDGenerator.generate("BIL"));
        try {
            sqlSession.insert("gmair.bill.info.insert", billInfo);
            result.setData(billInfo);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(BillInfo billInfo) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.bill.info.update", billInfo);
            result.setData(billInfo);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public  ResultData delete(String billId) {
        ResultData result = new ResultData();
        try {
            sqlSession.delete("gmair.bill.info.delete",billId);
        } catch ( Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
