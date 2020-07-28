package finley.gmair.service;

import finley.gmair.model.machine.MapModelConsumer;
import finley.gmair.util.ResultData;

public interface MapModelConsumerService {

    public int deleteByPrimaryKey(Integer mmcId);

    public int insert(MapModelConsumer record);

    public int insertSelective(MapModelConsumer record);

    public MapModelConsumer selectByPrimaryKey(Integer mmcId);

    public int updateByPrimaryKeySelective(MapModelConsumer record);

    public int updateByPrimaryKey(MapModelConsumer record);

    /**
     * 根据modelId返回相关联的耗材信息，并封装在ResultData中
     *
     * @param [modelId] 设备型号
     * @return finley.gmair.util.ResultData
     * @author zm
     * @date 2020/7/28 0028 15:54
     **/
    public ResultData getModelConsumerMap(String modelId);
}
