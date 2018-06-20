package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ScreenRecordDao;
import finley.gmair.model.machine.v2.ScreenRecord;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/19
 */
@Repository
public class ScreenRecordDaoImpl extends BaseDao implements ScreenRecordDao {
    @Override
    public ResultData selectScreenRecord(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<ScreenRecord> list = sqlSession.selectList("gmair.machine.screen.record.query", condition);
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
    public ResultData insertScreenRecord(ScreenRecord record) {
        ResultData result = new ResultData();
        record.setRecordId(IDGenerator.generate("SRR"));
        try {
            sqlSession.insert("gmair.machine.screen.record.insert", record);
            result.setData(record);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}