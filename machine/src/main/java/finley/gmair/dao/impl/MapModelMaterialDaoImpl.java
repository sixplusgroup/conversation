package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MapModelMaterialDao;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description TODO
 * @Author Administrator
 * @Date 2020/7/29 0029 15:07
 **/
@Repository
public class MapModelMaterialDaoImpl extends BaseDao implements MapModelMaterialDao {

    @Override
    public ResultData selectMaterialLinkByModelId(String modelId) {
        ResultData result = new ResultData();
        try {
            List<String> materialLinkList = sqlSession.selectList(
                    "gmair.machine.modelMaterial.selectMaterialLinkByModelId", modelId);
            if(materialLinkList.size() != 1){
                throw new Exception("no map-modelId-material info or multiple mapping");
            }
            result.setData(materialLinkList.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            result.setDescription(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }
}
