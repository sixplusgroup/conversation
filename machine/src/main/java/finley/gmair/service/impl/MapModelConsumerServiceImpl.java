package finley.gmair.service.impl;

import finley.gmair.dao.MapModelConsumerDao;
import finley.gmair.service.MapModelConsumerService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import finley.gmair.model.machine.MapModelConsumer;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapModelConsumerServiceImpl implements MapModelConsumerService {

    @Resource
    private MapModelConsumerDao mapModelConsumerDao;


    @Override
    public int deleteByPrimaryKey(Integer mmcId) {
        return mapModelConsumerDao.deleteByPrimaryKey(mmcId);
    }

    @Override
    public int insert(MapModelConsumer record) {
        return mapModelConsumerDao.insert(record);
    }

    @Override
    public int insertSelective(MapModelConsumer record) {
        return mapModelConsumerDao.insertSelective(record);
    }

    @Override
    public MapModelConsumer selectByPrimaryKey(Integer mmcId) {
        return mapModelConsumerDao.selectByPrimaryKey(mmcId);
    }

    @Override
    public int updateByPrimaryKeySelective(MapModelConsumer record) {
        return mapModelConsumerDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(MapModelConsumer record) {
        return mapModelConsumerDao.updateByPrimaryKey(record);
    }

    @Override
    public ResultData getModelConsumerMap(String modelId) {
        ResultData resultData = new ResultData();

        List<MapModelConsumer> mapModelConsumers;

        try {
            mapModelConsumers  = mapModelConsumerDao.selectAllByModelId(modelId);
        }catch (Exception e){
            e.printStackTrace();
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription(new StringBuffer("Fail to load the modelId-consumer information").toString());
            return resultData;
        }

        resultData.setResponseCode(ResponseCode.RESPONSE_OK);
        resultData.setData(mapModelConsumers);
        resultData.setDescription(new StringBuffer("success to load modelConsumerMap").toString());

        return resultData;
    }
}
