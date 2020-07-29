package finley.gmair.service.impl;

import finley.gmair.dao.MapModelMaterialDao;
import finley.gmair.service.MapModelMaterialService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import finley.gmair.model.machine.MapModelMaterial;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapModelMaterialServiceImpl implements MapModelMaterialService {

    @Resource
    private MapModelMaterialDao mapModelMaterialDao;


    @Override
    public int deleteByPrimaryKey(Integer mmcId) {
        return mapModelMaterialDao.deleteByPrimaryKey(mmcId);
    }

    @Override
    public int insert(MapModelMaterial record) {
        return mapModelMaterialDao.insert(record);
    }

    @Override
    public int insertSelective(MapModelMaterial record) {
        return mapModelMaterialDao.insertSelective(record);
    }

    @Override
    public MapModelMaterial selectByPrimaryKey(Integer mmcId) {
        return mapModelMaterialDao.selectByPrimaryKey(mmcId);
    }

    @Override
    public int updateByPrimaryKeySelective(MapModelMaterial record) {
        return mapModelMaterialDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(MapModelMaterial record) {
        return mapModelMaterialDao.updateByPrimaryKey(record);
    }


    @Override
    public ResultData getMaterial(String modelId) {
        ResultData resultData = new ResultData();
        String materialLink = "";

        try {
            List<String> linkList = mapModelMaterialDao.selectMaterialLinkByModelId(modelId);
            if (linkList.size() != 1) {
                throw new Exception("modelId对应多个materialLink");
            }
            materialLink = linkList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription(new StringBuffer("Fail to load the modelId-materialLink").toString());
            return resultData;
        }

        resultData.setResponseCode(ResponseCode.RESPONSE_OK);
        resultData.setData(materialLink);
        resultData.setDescription(new StringBuffer("success to load modelId-materialLink").toString());

        return resultData;
    }
}
