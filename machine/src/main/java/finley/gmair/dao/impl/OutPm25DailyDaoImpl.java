package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.OutPm25DailyDao;
import finley.gmair.model.machine.OutPm25Daily;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class OutPm25DailyDaoImpl extends BaseDao implements OutPm25DailyDao {

    @Override
    public ResultData insert(OutPm25Daily outPm25Daily){
        ResultData result = new ResultData();
        outPm25Daily.setRecordId(IDGenerator.generate("REC"));
        try{
            sqlSession.insert("gmair.machine.outPm25Daily.insert",outPm25Daily);
            result.setData(outPm25Daily);
        }catch (Exception e){
            e.printStackTrace();
            result.setDescription(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            List<OutPm25Daily> list = sqlSession.selectList("gmair.machine.outPm25Daily.query",condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        }catch (Exception e){
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }


}
