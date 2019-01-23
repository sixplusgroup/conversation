package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ComponentMeanDao;
import finley.gmair.model.dataAnalysis.ComponentMean;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ComponentMeanDaoImpl extends BaseDao implements ComponentMeanDao {

    @Override
    public ResultData query(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            List<ComponentMean> list = sqlSession.selectList("gmair.analysis.component.mean.query",condition);
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

    @Override
    public ResultData insertBatch(List<ComponentMean> list) {
        ResultData result = new ResultData();
        for (ComponentMean cm: list)
            cm.setRecordId(IDGenerator.generate("CMI"));
        try {
            sqlSession.insert("gmair.analysis.component.mean.insertBatch", list);
            result.setData(list);
        }catch (Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}
