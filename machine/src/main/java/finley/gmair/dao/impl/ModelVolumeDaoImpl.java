package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ModelVolumeDao;
import finley.gmair.model.machine.ModelVolume;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ModelVolumeDaoImpl extends BaseDao implements ModelVolumeDao {

    @Override
    public ResultData insert(ModelVolume modelVolume){
        ResultData result = new ResultData();
        modelVolume.setConfigId(IDGenerator.generate("MVO"));
        try{
            sqlSession.insert("gmair.machine.modelvolume.insert",modelVolume);
            result.setData(modelVolume);
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
            List<ModelVolume> list = sqlSession.selectList("gmair.machine.modelvolume.query",condition);
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
            sqlSession.update("gmair.machine.modelvolume.updateByModelId",condition);
        }catch (Exception e){
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryTurboVolumeValue(String modelId) {
        ResultData result = new ResultData();
        Map<String,Object> condition = new HashMap<>(1);
        condition.put("modelId",modelId);
        try {
            String turboVolume = sqlSession.selectOne("gmair.machine.modelvolume.queryTurboVolume", condition);
            if (turboVolume == null || StringUtils.isEmpty(turboVolume)) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            else {
                result.setData(turboVolume);
            }
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
