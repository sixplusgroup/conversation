package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ModelEfficientConfigDao;
import finley.gmair.model.machine.ModelEfficientConfig;
import finley.gmair.model.machine.ModelLight;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ModelEfficientConfigImpl extends BaseDao implements ModelEfficientConfigDao {

    @Override
    public ResultData insert(ModelEfficientConfig modelEfficientConfig){
        ResultData result = new ResultData();
        modelEfficientConfig.setConfigId(IDGenerator.generate("MET"));
        try{
            sqlSession.insert("gmair.machine.model_efficient_config.insert",modelEfficientConfig);
            result.setData(modelEfficientConfig);
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
            List<ModelLight> list = sqlSession.selectList(
                    "gmair.machine.model_efficient_config.query",condition);
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
    public ResultData updateByModelId(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            sqlSession.update("gmair.machine.model_efficient_config.updateByModelId",condition);
        }catch (Exception e){
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
