package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ModelEnabledComponentDao;
import finley.gmair.dao.ModelLightDao;
import finley.gmair.model.machine.ModelEnabledComponent;
import finley.gmair.model.machine.ModelLight;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ModelEnabledComponentDaoImpl extends BaseDao implements ModelEnabledComponentDao {

    @Override
    public ResultData insert(ModelEnabledComponent modelEnabledComponent){
        ResultData result = new ResultData();
        modelEnabledComponent.setMecId(IDGenerator.generate("MEC"));
        try{
            sqlSession.insert("gmair.machine.model_enabled_component.insert",modelEnabledComponent);
            result.setData(modelEnabledComponent);
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
            List<ModelLight> list = sqlSession.selectList("gmair.machine.model_enabled_component.query",condition);
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
