package finley.gmair.dao.Impl;

import finley.gmair.dao.CheckRecordDao;
import finley.gmair.model.assemble.CheckRecord;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CheckRecordImpl extends BaseDao implements CheckRecordDao {

    @Override
    public ResultData insert(CheckRecord checkRecord) {
        ResultData result = new ResultData();
        checkRecord.setRecordId(IDGenerator.generate("CRH"));
        try {
            sqlSession.insert("gmair.assemble.check_record.insert", checkRecord);
            result.setData(checkRecord);
        } catch (Exception e) {
            e.printStackTrace();
            result.setDescription(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<CheckRecord> list = sqlSession.selectList("gmair.assemble.check_record.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.assemble.check_record.update", condition);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
