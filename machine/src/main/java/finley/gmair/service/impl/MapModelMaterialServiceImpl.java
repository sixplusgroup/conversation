package finley.gmair.service.impl;

import finley.gmair.dao.MapModelMaterialDao;
import finley.gmair.service.MapModelMaterialService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class MapModelMaterialServiceImpl implements MapModelMaterialService {

    @Resource
    private MapModelMaterialDao mapModelMaterialDao;

    @Override
    public ResultData getMaterial(String modelId) {
        ResultData result = new ResultData();
        ResultData response = mapModelMaterialDao.selectMaterialLinkByModelId(modelId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to get  material information of the modelId").toString());
            return result;
        }

        Map<String, String> resMap = new HashMap<>(1);
        resMap.put("materialsLink", String.valueOf(response.getData()));

        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(resMap);
        result.setDescription(new StringBuffer("Success to load material information of the modelId").toString());
        return result;
    }
}
